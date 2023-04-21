package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.CommentContentDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentPageDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateCommentDTO;
import com.it_talends_goodreads.goodreads.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comments")
    public CommentWithoutOwnerDTO create(@Valid @RequestBody CreateCommentDTO createCommentDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return commentService.create(createCommentDTO, userId);
    }

    @PutMapping("/comments/{id}")
    public CommentWithoutOwnerDTO update(@PathVariable int id, @Valid @RequestBody CommentContentDTO commentContentDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return commentService.update(commentContentDTO, userId, id);
    }

    @DeleteMapping("/comments/{id}")
    public String delete(@PathVariable int id, HttpSession session) {
        int userId = getLoggedId(session);
        commentService.delete(id, userId);
        return "You delete comment with id " + id;
    }
    @GetMapping("/comments/reviews/{id}")
    public CommentPageDTO getAllByReview(@PathVariable int id, int pageN, int recordCount) {
        return commentService.getAllByReview(id, pageN, recordCount);
    }
}
