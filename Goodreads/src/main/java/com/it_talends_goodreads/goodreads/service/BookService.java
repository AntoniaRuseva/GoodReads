package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.BookDetailedInfoDTO;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.Category;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService extends AbstractService {
    @Autowired
    private BookRepository bookRepository;

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
}
