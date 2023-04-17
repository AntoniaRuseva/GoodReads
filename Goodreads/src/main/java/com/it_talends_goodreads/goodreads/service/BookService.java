package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;

import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class BookService extends AbstractService {

    @PersistenceContext
    private EntityManager entityManager;
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
    public BookRatingDTO rate(int bookId, BookRateDTO bookRateDTO, int userId) {

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new NotFoundException("No such book");
        }
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        if (bookRepository.findByRateByd(user).isPresent()) {
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

    public List<BookCommonInfoDTO> getBooksByFilters(BooksCharacteristicDTO booksCharacteristicDTO) {

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
//       JPAQuery query = new JPAQuery(entityManager);
        QBook book = QBook.book;

//        List<Book> books = new ArrayList<>();

//        if (booksCharacteristicDTO.getAuthor() != null) {
        List<Book> b = query.selectFrom(book).where(book.author.name.eq(booksCharacteristicDTO.getAuthorName())).fetch();

//        }
//        if (booksCharacteristicDTO.getCategory1() != null) {
//           b = query.selectFrom(book).where(book.categories.contains(booksCharacteristicDTO.getCategory1())).fetch();
//        }
//        if (booksCharacteristicDTO.getCategory2() != null) {
//            query.selectFrom(book).where(book.categories.contains(booksCharacteristicDTO.getCategory2())).fetch();
//        }
//        if (booksCharacteristicDTO.getCategory3() != null) {
//            query.selectFrom(book).where(book.categories.contains(booksCharacteristicDTO.getCategory3())).fetch();
//        }
//        if (booksCharacteristicDTO.getReleasedDate() != null) {
//            query.selectFrom(book).where(book.releasedDate.after(booksCharacteristicDTO.getReleasedDate())).fetch();
//        }
//        if (booksCharacteristicDTO.getLanguage() != null) {
//            query.selectFrom(book).where(book.language.eq(booksCharacteristicDTO.getLanguage())).fetch();
//        }
//        if (booksCharacteristicDTO.getFormat() != null) {
//            query.selectFrom(book).where(book.format.eq(booksCharacteristicDTO.getFormat())).fetch();
//        }
//        if (booksCharacteristicDTO.getPages() != 0) {
//            query.selectFrom(book).where(book.pages.between(0, booksCharacteristicDTO.getPages())).fetch();
//        }
//        if (booksCharacteristicDTO.getRating() != null) {
//            query.selectFrom(book).where(book.rating.between(1, booksCharacteristicDTO.getRating())).fetch();

            return b.stream().map(bk -> mapper.map(bk, BookCommonInfoDTO.class)).collect(Collectors.toList());

    }
}
