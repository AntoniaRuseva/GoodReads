package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.CommentContentDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateCommentDTO;
import com.it_talends_goodreads.goodreads.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comments")
    public CommentWithoutOwnerDTO create(@RequestBody CreateCommentDTO createCommentDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return commentService.create(createCommentDTO, userId);
    }

    @PutMapping("/comments/{id}")
    public CommentWithoutOwnerDTO update(@PathVariable("id") int id, @RequestBody CommentContentDTO commentContentDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return commentService.update(commentContentDTO, userId, id);
    }

    @DeleteMapping("/comments/{id}")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        int userId = getLoggedId(session);
        commentService.delete(id, userId);
        return "You delete comment with id " + id;
    }

    @GetMapping("/comments/reviews/{id}")
    public List<CommentWithoutOwnerDTO> getAllByReview(@PathVariable("id") int id, HttpSession session) {
        return commentService.getAllByReview(id);
    }
}