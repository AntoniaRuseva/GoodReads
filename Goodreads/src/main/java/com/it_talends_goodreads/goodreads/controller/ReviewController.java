package com.it_talends_goodreads.goodreads.controller;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewDTO;
import com.it_talends_goodreads.goodreads.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController extends AbstractController{
    @Autowired
    private ReviewService reviewService;
    @PostMapping("/reviews/books/{id}")
    public ReturnReviewDTO createReview(@PathVariable int id, @RequestBody CreateReviewDTO dto, HttpSession s){
        int userId=getLoggedId(s);
       return reviewService.createReview(dto,userId,id);
    }
}
