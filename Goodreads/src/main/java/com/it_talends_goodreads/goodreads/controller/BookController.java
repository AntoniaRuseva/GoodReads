package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.service.BookService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController extends AbstractController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books/{id}")
    public BookDetailedInfoDTO getInfoByID(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }
    @GetMapping("/books/users/{id}")
    public List<BookCommonInfoDTO> getByUserID(@PathVariable("id") int id) {
        return bookService.getByUserID(id);
    }
    @PutMapping("/books/{id}")
    public BookRatingDTO rate(@PathVariable("id") int bookId, @Valid @RequestBody BookRateDTO bookRateDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return bookService.rate(bookId,bookRateDTO, userId);
    }
}