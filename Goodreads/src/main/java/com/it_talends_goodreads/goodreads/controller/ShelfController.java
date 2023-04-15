package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.service.ShelfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShelfController extends AbstractController {
    @Autowired
    private ShelfService shelfService;
    @PostMapping("/shelves")
    public ShelfWithoutOwnerAndBooksDTO create(@RequestBody CreateShelfDTO createShelfDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return shelfService.createShelf(createShelfDTO, userId);
    }

    @GetMapping("/shelves/users/{id}")
    public List<ShelfWithOutUserDTO> getAllShelvesByUSer(@PathVariable("id") int id) {
        return shelfService.getAllShelvesByUSer(id);
    }
}
