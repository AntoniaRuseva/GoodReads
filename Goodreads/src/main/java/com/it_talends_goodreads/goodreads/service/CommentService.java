package com.it_talends_goodreads.goodreads.service;


import com.it_talends_goodreads.goodreads.model.DTOs.CommentContentDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateCommentDTO;
import com.it_talends_goodreads.goodreads.model.entities.Comment;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public CommentWithoutOwnerDTO create(CreateCommentDTO createCommentDTO, int userId) {
        User user = getUserById(userId);
        Comment comment;

        Optional<Review> optional = reviewRepository.findById(createCommentDTO.getReviewId());
        if (optional.isEmpty()) {
            throw new NotFoundException("No such review");
        }
        if (createCommentDTO.getParentId() != 0) {
            Optional<Comment> optionalParent = commentRepository.findById(createCommentDTO.getParentId());
            if (optionalParent.isEmpty()) {
                throw new NotFoundException("No such parent comment");
            }
            Comment parent = optionalParent.get();
            comment = Comment
                    .builder()
                    .writer(user)
                    .review(optional.get())
                    .parent(parent)
                    .content(createCommentDTO.getContent())
                    .build();
        } else {
            comment = Comment
                    .builder()
                    .writer(user)
                    .review(optional.get())
                    .parent(null)
                    .content(createCommentDTO.getContent())
                    .build();
        }
        commentRepository.save(comment);
        return CommentWithoutOwnerDTO
                .builder()
                .id(comment.getId())
                .parentId(0)
                .writerName(comment.getWriter().getUserName())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .build();
    }

    @Transactional
    public CommentWithoutOwnerDTO update(CommentContentDTO commentContentDTO, int userId, int commentId) {

        Comment comment = exists(commentId);
        if (authorized(userId, comment)) {
            comment.setContent(commentContentDTO.getContent());
            commentRepository.save(comment);
        }
        return CommentWithoutOwnerDTO
                .builder()
                .id(commentId)
                .writerName(comment.getWriter().getUserName())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .build();
    }

    private Comment exists(int id) {
        Optional<Comment> optional = commentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such comment");
        }
        return optional.get();
    }

    private boolean authorized(int userId, Comment comment) {
        if (userId != comment.getWriter().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }

    public void delete(int id, int userId) {
        Comment comment = exists(id);
        if (authorized(userId, comment)) {
            commentRepository.deleteById(id);
        }
    }

    public List<CommentWithoutOwnerDTO> getAllByReview(int id) {
        Optional<Review> optional = reviewRepository.findById(id);
        if(optional.isEmpty()){
            throw new NotFoundException("No such review");
        }
        Review review = optional.get();
        List<Comment> comments = commentRepository.findAllByReview(review);
        return comments
                .stream()
                .map(c->CommentWithoutOwnerDTO
                        .builder()
                        .id(c.getId()).reviewId(id)
                        .writerName(c.getWriter().getUserName())
                        .content(c.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
