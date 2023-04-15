package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;

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

    @Transactional
    public ShelfWithoutOwnerAndBooksDTO createShelf(CreateShelfDTO createShelfDTO, int userId) {
        User user = getUserById(userId);
        Shelf shelf = new Shelf();
        shelf.setUser(user);
        shelf.setName(createShelfDTO.getName());
        shelfRepository.save(shelf);
        return mapper.map(shelf, ShelfWithoutOwnerAndBooksDTO.class);
    }

    public List<ShelfWithoutUserDTO> getAllShelvesByUSer(int userId) {
        User user = getUserById(userId);
        List<Shelf> list = shelfRepository.findAllByUser(user);
        return list.stream()
                .map(s -> ShelfWithoutUserDTO
                        .builder()
                        .id(s.getId())
                        .name(s.getName())
                        .countBooksInTheShelve(s.getBooksShelves().size())
                        .build()
                ).toList();
    }

    public ShelfWithBookInfoDTO getShelfById(int id) {
        Optional<Shelf> optional = shelfRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such shelf");
        }
        Shelf shelf = optional.get();
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
        return mapper.map(shelf, ShelfWithoutOwnerAndBooksDTO.class);
    }

    @Transactional
    public void delete(int id, int userId) {
        Shelf shelf = exists(id);
        if (authorized(userId, shelf)) {
            shelfRepository.deleteById(id);
        }
    }

    private Shelf exists(int id) {
        Optional<Shelf> optional = shelfRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such shelf");
        }
        return optional.get();
    }

    private boolean authorized(int userId, Shelf shelf) {
        if (userId != shelf.getUser().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }

    @Transactional
    public ShelfWithBookInfoDTO addBook(int shelfId, int bookId, int userId) {
        Shelf shelf = exists(shelfId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new NotFoundException("No such book");
        }
        if (authorized(userId, shelf)) {
            BooksShelves booksShelves = BooksShelves.builder().book(book.get()).shelf(shelf).dateAdded(LocalDate.now()).build();
            booksShelvesRepository.save(booksShelves);
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
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new NotFoundException("No such book");
        }
        Optional<BooksShelves> bookShelve = booksShelvesRepository.findBooksShelvesByBookAndShelf(book.get(), shelf);
        if (bookShelve.isEmpty()) {
            throw new NotFoundException("No such combination book-shelf");
        }
        if (authorized(userId, shelf)) {
            booksShelvesRepository.delete(bookShelve.get());
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
