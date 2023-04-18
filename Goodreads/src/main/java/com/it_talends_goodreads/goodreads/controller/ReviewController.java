package com.it_talends_goodreads.goodreads.controller;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewWithoutBookDTO;
import com.it_talends_goodreads.goodreads.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController extends AbstractController{
    @Autowired
    private ReviewService reviewService;
    @PostMapping("/reviews/books/{id}")
    public ReturnReviewDTO createReview(@PathVariable int id, @Valid @RequestBody CreateReviewDTO dto, HttpSession s){
        int userId=getLoggedId(s);
       return reviewService.createReview(dto,userId,id);
    }
    @DeleteMapping("/reviews/{id}")
    public String deleteReview(@PathVariable("id") int id,HttpSession s){
        int userId=getLoggedId(s);
        return reviewService.deleteReview(id,userId);
    }
    @PutMapping("/reviews/{id}")
    public ReturnReviewDTO updateReview(@PathVariable("id")int id,@Valid @RequestBody CreateReviewDTO dto, HttpSession s){
        getLoggedId(s);
        return reviewService.updateReview(id,dto);
    }
    @GetMapping("/reviews/books/{id}")
    public List<ReturnReviewWithoutBookDTO> getAllReviewsForBook(@PathVariable("id")int id){
        return reviewService.getAllReviews(id);
    }
    @PostMapping("/reviews/{id}")
    public ReturnReviewWithoutBookDTO likeReview(@PathVariable("id") int id,HttpSession s){
        int userId=getLoggedId(s);
       return reviewService.likeReview(id,userId);
    }
}
