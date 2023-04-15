package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.BookCommonInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.BookInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ReturnReviewWithoutBookDTO;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.Review;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReviewService extends AbstractService {
    @Autowired
    private ReviewRepository reviewRepository;


    public ReturnReviewDTO createReview(CreateReviewDTO dto, int userId, int bookId) {
        User u = getUserById(userId);
        Optional<Book> existBook = bookRepository.findById(bookId);
        if (existBook.isEmpty()) {
            throw new BadRequestException("Book doesn't exist.");
        }
        Review review = new Review();
        review.setBook(existBook.get());
        review.setWriter(u);
        review.setContent(dto.getContent());
        review.setDate(LocalDate.now());
        reviewRepository.save(review);
        ReturnReviewDTO returnReviewDTO = mapper.map(review, ReturnReviewDTO.class);
        returnReviewDTO.setBookInfo(mapper.map(existBook.get(), BookCommonInfoDTO.class));
        return returnReviewDTO;
    }

    public String deleteReview(int id, int userId) {
        Review rev = checkIfReviewExists(id);
        if (authorized(id, rev)) {
            reviewRepository.deleteById(id);
        }
        return "You have deleted review with id: " + id;
    }

    public ReturnReviewDTO updateReview(int id, int userId, CreateReviewDTO dto) {
        Review rev = checkIfReviewExists(id);
        if (authorized(id, rev)) {
            rev.setContent(dto.getContent());
            reviewRepository.save(rev);
        }
        ReturnReviewDTO returnReviewDTO = mapper.map(rev, ReturnReviewDTO.class);
        returnReviewDTO.setBookInfo(mapper.map(rev.getBook(), BookCommonInfoDTO.class));
        return returnReviewDTO;
    }

    public List<ReturnReviewWithoutBookDTO> getAllReviews(int id) {
        List<Review> list = reviewRepository.getAllByBookId(id);
        List<ReturnReviewWithoutBookDTO> returnList =
                list.stream().map(r -> mapper.map(r, ReturnReviewWithoutBookDTO.class))
                        .collect(Collectors.toList());
        return returnList;
    }

    public ReturnReviewWithoutBookDTO likeReview(int id, int userId) {
        Review rev = checkIfReviewExists(id);
        Optional<User> u = userRepository.findById(userId);
        if (rev.getLikedBy().contains(u.get())) {
            rev.getLikedBy().remove(u.get());
        } else {
            rev.getLikedBy().add(u.get());
        }
        reviewRepository.save(rev);
        ReturnReviewWithoutBookDTO returnInst = mapper.map(rev, ReturnReviewWithoutBookDTO.class);
        returnInst.setLikes(rev.getLikedBy().size());
        return returnInst;
    }

    private Review checkIfReviewExists(int id) {
        Optional<Review> rev = reviewRepository.findById(id);
        if (rev.isEmpty()) {
            throw new NotFoundException("This review doesn't exist.");
        }
        return rev.get();
    }

    private boolean authorized(int userId, Review rev) {
        if (userId != rev.getWriter().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes.");
        }
        return true;
    }
}
