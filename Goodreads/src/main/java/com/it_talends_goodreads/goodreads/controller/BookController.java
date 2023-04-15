package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.BookDetailedInfoDTO;
import com.it_talends_goodreads.goodreads.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController extends AbstractController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books/{id}")
    public BookDetailedInfoDTO getInfoByID(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }
}