package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.BookCommonInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.BookRateDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.BookRatingDTO;
import com.it_talends_goodreads.goodreads.model.entities.BooksShelves;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import org.springframework.stereotype.Service;
import com.it_talends_goodreads.goodreads.model.DTOs.BookDetailedInfoDTO;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.Category;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class BookService extends AbstractService {


    @Autowired
    private BooksShelvesRepository booksShelvesRepository;

    public BookDetailedInfoDTO getBookById(int id) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("No such book");
        }
        Book book = optional.get();
        return BookDetailedInfoDTO
                .builder()
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .categories(book.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()))
                .format(book.getFormat())
                .isbn(book.getIsbn())
                .pages(book.getPages())
                .language(book.getLanguage())
                .description(book.getDescription())
                .rating(book.getRating())
                .releasedDate(book.getReleasedDate())
                .rateCounter(book.getRateCounter())
                .reviewsCounter(book.getReviews().size())
                .build();
    }

    public List<BookCommonInfoDTO> getByUserID(int userId) {
        List<BooksShelves> booksShelves = booksShelvesRepository.getBooksShelvesByShelf_UserId(userId);
        if (booksShelves.isEmpty()) {
            throw new NotFoundException("This user doesn't have book on his/her shelves");
        }
        return booksShelves
                .stream()
                .map(BooksShelves::getBook)
                .map(b -> BookCommonInfoDTO
                        .builder()
                        .id(b.getId())
                        .title(b.getTitle())
                        .authorName(b.getAuthor().getName())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public BookRatingDTO rate(int bookId, BookRateDTO bookRateDTO,int userId) {

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new NotFoundException("No such book");
        }
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        if(bookRepository.findByRateByd(user).isPresent()){
            throw new UnauthorizedException("you have already rated this book");
        }
        double curRating = book.get().getRating();
        int curRateCounter = book.get().getRateCounter();
        double newRating = (curRating * curRateCounter + bookRateDTO.getRating()) / (curRateCounter + 1);
        book.get().setRating(newRating);
        book.get().setRateCounter(curRateCounter + 1);
        bookRepository.save(book.get());

        return BookRatingDTO
                .builder()
                .rating(String.format("%.2f", curRating))
                .counter(curRateCounter).build();
    }
}
