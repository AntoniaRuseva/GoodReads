package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.service.ShelfService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShelfController extends AbstractController {
    @Autowired
    private ShelfService shelfService;
    @PostMapping("/shelves")
    public ShelfWithoutOwnerAndBooksDTO create(@Valid @RequestBody CreateShelfDTO createShelfDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return shelfService.createShelf(createShelfDTO, userId);
    }

    @GetMapping("/shelves/users/{id}")
    public List<ShelfWithoutUserDTO> getAllShelvesByUSer(@PathVariable("id") int id) {
        return shelfService.getAllShelvesByUSer(id);
    }

    @GetMapping("/shelves/{id}")
    public ShelfWithBookInfoDTO getInfoByID(@PathVariable("id") int id) {
        return shelfService.getShelfById(id);
    }

    @PutMapping("/shelves/{id}")
    public ShelfWithoutOwnerAndBooksDTO update(@PathVariable("id") int id, @RequestBody CreateShelfDTO createShelfDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return shelfService.update(id, userId, createShelfDTO);
    }

    @DeleteMapping("/shelves/{id}")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        int userId = getLoggedId(session);
        shelfService.delete(id, userId);
        return "You delete shelf with id " + id;
    }

    @PutMapping("/shelves/{sid}/books/{bid}")
    public ShelfWithBookInfoDTO addBook(@PathVariable("sid") int shelfId, @PathVariable("bid") int bookId, HttpSession session) {
        int userId = getLoggedId(session);
        return shelfService.addBook(shelfId, bookId, userId);
    }

    @PutMapping("/shelves/{sid}/books/{bid}/remove")
    public ShelfWithBookInfoDTO removeBook(@PathVariable("sid") int shelfId, @PathVariable("bid") int bookId, HttpSession session) {
        int userId = getLoggedId(session);
        return shelfService.removeBook(shelfId, bookId, userId);
    }

}
