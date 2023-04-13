package com.it_talends_goodreads.goodreads.service;
import com.it_talends_goodreads.goodreads.model.DTOs.BookInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewDTO;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.Review;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class ReviewService extends AbstractService{
    @Autowired
    private ReviewRepository reviewRepository;


    public ReturnReviewDTO createReview(CreateReviewDTO dto, int userId, int bookId){
        //is user logged in, get user
        User u = getUserById(userId);
        //is the id of the book valid, get book
        Optional<Book> existBook= bookRepository.findById(bookId);
        if(existBook.isEmpty()){
            throw new BadRequestException("Book doesn't exist.");
        }
        Review review= new Review();
        review.setBook(existBook.get());
        review.setWriter(u);
        review.setContent(dto.getContent());
        review.setDate(LocalDate.now());
        reviewRepository.save(review);
        ReturnReviewDTO returnReviewDTO = mapper.map(review,ReturnReviewDTO.class);
        returnReviewDTO.setBookInfo(mapper.map(existBook.get(), BookInfoDTO.class));
        return returnReviewDTO;
    }
}
