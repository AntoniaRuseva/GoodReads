package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;

import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@Service

public class BookService extends AbstractService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BooksShelvesRepository booksShelvesRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookDetailedInfoDTO getBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("\"No such book\""));
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

    public BookPageDTO getByUserID(int userId,int pageN,int recordCount) {
        Pageable pageable = PageRequest.of(pageN, recordCount);
        Page<BooksShelves> booksShelves = booksShelvesRepository.getBooksShelvesByShelf_UserId(userId,pageable);
        if (booksShelves.isEmpty()) {
            throw new NotFoundException("This user doesn't have book on his/her shelves");
        }
        int totalPages = booksShelves.getTotalPages();
        return BookPageDTO.builder().currentPage(pageN).totalPages(totalPages).books(booksShelves
                .stream()
                .map(BooksShelves::getBook)
                .map(b -> BookCommonInfoDTO
                        .builder()
                        .id(b.getId())
                        .title(b.getTitle())
                        .authorName(b.getAuthor().getName())
                        .build())
                .collect(Collectors.toList())).build();

    }

    public BookRatingDTO rate(int bookId, BookRateDTO bookRateDTO, int userId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("No such book"));
        User user = getUserById(userId);
        if (bookRepository.findByRateByd(user).isPresent()) {
            throw new UnauthorizedException(String.format("User with id  %d can't rate book with id %d, because already did it",
                    userId, bookId, bookRateDTO.getRating()));
        }
        double curRating = book.getRating();
        int curRateCounter = book.getRateCounter();
        double newRating = (curRating * curRateCounter + bookRateDTO.getRating()) / (curRateCounter + 1);

        book.setRating(newRating);
        book.setRateCounter(curRateCounter + 1);
        bookRepository.save(book);
        logger.info(String.format("User with id  %d rated book with id %d with rate %d", userId, bookId, bookRateDTO.getRating()));
        return BookRatingDTO
                .builder()
                .rating(String.format("%.2f", curRating))
                .counter(curRateCounter).build();
    }
    public List<BookCommonInfoDTO> getBooksByFilters(BooksCharacteristicDTO booksCharacteristicDTO) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QBook book = QBook.book;
        BooleanExpression baseQuery = book.isNotNull();

        if (booksCharacteristicDTO.getAuthorName() != null) {
            baseQuery = baseQuery.and(book.author.name.eq(booksCharacteristicDTO.getAuthorName()));
        }
        if (booksCharacteristicDTO.getCategories() != null) {
            int size = booksCharacteristicDTO.getCategories().size();
            for (int i = 0; i < size; i++) {
                baseQuery = baseQuery.and(book.categories.contains(booksCharacteristicDTO.getCategories().get(i)));
            }
        }
        if (booksCharacteristicDTO.getReleasedDate() != null) {
            baseQuery = baseQuery.and(book.releasedDate.after(booksCharacteristicDTO.getReleasedDate()));
        }
        if (booksCharacteristicDTO.getLanguage() != null) {
            baseQuery = baseQuery.and(book.language.eq(booksCharacteristicDTO.getLanguage()));
        }
        if (booksCharacteristicDTO.getFormat() != null) {
            baseQuery = baseQuery.and(book.format.eq(booksCharacteristicDTO.getFormat()));
        }
        if (booksCharacteristicDTO.getPages() != 0) {
            baseQuery = baseQuery.and(book.pages.between(0, booksCharacteristicDTO.getPages()));
        }
        if (booksCharacteristicDTO.getRating() != null) {
            baseQuery = baseQuery.and(book.rating.between(1, booksCharacteristicDTO.getRating()));
        }
        List<Book> b = query.selectFrom(book).where(baseQuery).fetch();
        return b.stream().map(bk -> mapper.map(bk, BookCommonInfoDTO.class)).collect(Collectors.toList());
    }
}
