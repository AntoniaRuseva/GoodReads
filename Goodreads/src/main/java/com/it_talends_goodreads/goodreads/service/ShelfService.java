package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShelfService extends AbstractService {
    @Autowired
    private ShelfRepository shelfRepository;
    @Autowired
    private BooksShelvesRepository booksShelvesRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Transactional
    public ShelfWithoutOwnerAndBooksDTO createShelf(CreateShelfDTO createShelfDTO, int userId) {
        User user = getUserById(userId);
        Shelf shelf = new Shelf();
        shelf.setUser(user);
        shelf.setName(createShelfDTO.getName());
        shelfRepository.save(shelf);
        logger.info(String.format("User with id %d created shelf with id %d", userId, shelf.getId()));
        return mapper.map(shelf, ShelfWithoutOwnerAndBooksDTO.class);
    }

    public List<ShelfWithOutUserDTO> getAllShelvesByUser(int userId) {
        User user = getUserById(userId);
        List<Shelf> list = shelfRepository.findAllByUser(user);
        return list.stream()
                .map(s -> ShelfWithOutUserDTO
                        .builder()
                        .id(s.getId())
                        .name(s.getName())
                        .countBooksInTheShelve(s.getBooksShelves().size())
                        .build()
                ).toList();
    }

    public ShelfWithBookInfoDTO getShelfById(int id) {
        Shelf shelf = shelfRepository.findById(id).orElseThrow(() -> new BadRequestException("No such shelf"));

        List<Book> books = shelf
                .getBooksShelves()
                .stream()
                .filter(bs -> bs.getShelf().getId() == id)
                .map(BooksShelves::getBook)
                .toList();
        return ShelfWithBookInfoDTO
                .builder()
                .id(id)
                .name(shelf.getName())
                .books(books
                        .stream()
                        .map(b -> mapper.map(b, BookCommonInfoDTO.class))
                        .toList())
                .build();
    }

    @Transactional
    public ShelfWithoutOwnerAndBooksDTO update(int id, int userId, CreateShelfDTO createShelfDTO) {
        Shelf shelf = exists(id);
        if (authorized(userId, shelf)) {
            shelf.setName(createShelfDTO.getName());
            shelfRepository.save(shelf);
        }
        logger.info(String.format("User with id %d updated shelf with id %d", userId, shelf.getId()));
        return mapper.map(shelf, ShelfWithoutOwnerAndBooksDTO.class);
    }

    @Transactional
    public void delete(int id, int userId) {
        Shelf shelf = exists(id);
        if (authorized(userId, shelf)) {
            shelf.getBooksShelves().removeAll(shelf.getBooksShelves().stream().toList());
            shelfRepository.save(shelf);
            shelfRepository.deleteById(id);
            logger.info(String.format("User with id %d deleted shelf with id %d", userId, id));
        }
    }
    private Shelf exists(int id) {
        return shelfRepository.findById(id).orElseThrow(() -> new BadRequestException("No such shelf"));
    }

    private boolean authorized(int userId, Shelf shelf) {
        if (userId != shelf.getUser().getId()) {
            logger.info(String.format("User with id %d is trying to make changes on shelf with id %d, " +
                    "that doesn't belong to him", userId, shelf.getId()));
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }

    @Transactional
    public ShelfWithBookInfoDTO addBook(int shelfId, int bookId, int userId) {
        Shelf shelf = exists(shelfId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("No such book"));

        if (authorized(userId, shelf)) {
            BooksShelves booksShelves = BooksShelves.builder().book(book).shelf(shelf).dateAdded(LocalDate.now()).build();
            booksShelvesRepository.save(booksShelves);
            logger.info(String.format("User with id %d added book with id %d to shelf with id %d", userId, bookId, shelfId));
        }
        List<Book> books = shelf.getBooksShelves().stream().map(BooksShelves::getBook).toList();
        return ShelfWithBookInfoDTO
                .builder()
                .id(shelfId)
                .name(shelf.getName())
                .books(books
                        .stream()
                        .map(b -> mapper.map(book, BookCommonInfoDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ShelfWithBookInfoDTO removeBook(int shelfId, int bookId, int userId) {
        Shelf shelf = exists(shelfId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("No such book"));
        BooksShelves bookShelve = booksShelvesRepository
                .findBooksShelvesByBookAndShelf(book, shelf).orElseThrow(() ->new NotFoundException("No such combination book-shelf"));
        if (authorized(userId, shelf)) {
            booksShelvesRepository.delete(bookShelve);
            logger.info(String.format("User with id %d remove book with id %d to shelf with id %d", userId, bookId, shelfId));
        }
        List<Book> books = shelf.getBooksShelves().stream().map(BooksShelves::getBook).toList();
        return ShelfWithBookInfoDTO
                .builder()
                .id(shelfId)
                .name(shelf.getName())
                .books(books
                        .stream()
                        .map(b -> mapper.map(book, BookCommonInfoDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
