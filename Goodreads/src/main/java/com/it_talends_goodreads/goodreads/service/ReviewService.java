package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.BookCommonInfoDTO;

import com.it_talends_goodreads.goodreads.model.DTOs.CreateReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewWithoutBookDTO;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.Review;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;

import com.it_talends_goodreads.goodreads.model.repositories.CommentRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReviewService extends AbstractService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public ReturnReviewDTO createReview(CreateReviewDTO dto, int userId, int bookId) {
        User u = getUserById(userId);
        Book existBook = bookRepository.findById(bookId).orElseThrow(()->new BadRequestException("Book doesn't exist."));
        Review review = new Review();
        review.setBook(existBook);
        review.setWriter(u);
        review.setContent(dto.getContent());
        review.setDate(LocalDate.now());
        reviewRepository.save(review);
        ReturnReviewDTO returnReviewDTO = mapper.map(review, ReturnReviewDTO.class);
        returnReviewDTO.setBookInfo(mapper.map(existBook, BookCommonInfoDTO.class));
        return returnReviewDTO;
    }
    @Transactional
    public String deleteReview(int id, int userId) {
        Review rev = checkIfReviewExists(id);
        if (authorized(id, rev)) {
            commentRepository.removeCommentsByReview(rev);
            reviewRepository.deleteById(id);
        }
        return "You have deleted review with id: " + id;
    }
    @Transactional
    public ReturnReviewDTO updateReview(int id, int userId, CreateReviewDTO dto) {
        Review rev = checkIfReviewExists(id);
        if (authorized(userId, rev)) {
            rev.setContent(dto.getContent());
            reviewRepository.save(rev);
        }
        ReturnReviewDTO returnReviewDTO = mapper.map(rev, ReturnReviewDTO.class);
        returnReviewDTO.setBookInfo(mapper.map(rev.getBook(), BookCommonInfoDTO.class));
        return returnReviewDTO;
    }

    public List<ReturnReviewWithoutBookDTO> getAllReviews(int id) {
        List<Review> list = reviewRepository.getAllByBookId(id);
        return list.stream()
                .map(r -> mapper.map(r, ReturnReviewWithoutBookDTO.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public ReturnReviewWithoutBookDTO likeReview(int id, int userId) {
        Review rev = checkIfReviewExists(id);
        User u = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found."));
        if (rev.getLikedBy().contains(u)) {
            rev.getLikedBy().remove(u);
        } else {
            rev.getLikedBy().add(u);
        }
        reviewRepository.save(rev);
        ReturnReviewWithoutBookDTO returnInst = mapper.map(rev, ReturnReviewWithoutBookDTO.class);
        returnInst.setLikes(rev.getLikedBy().size());
        return returnInst;
    }

    private Review checkIfReviewExists(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(()->new NotFoundException("This review doesn't exist."));
    }

    private boolean authorized(int userId, Review rev) {
        if (userId != rev.getWriter().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes.");
        }
        return true;
    }
}
