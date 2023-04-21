package com.it_talends_goodreads.goodreads.service;


import com.it_talends_goodreads.goodreads.model.DTOs.CommentContentDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentPageDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CommentWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateCommentDTO;
import com.it_talends_goodreads.goodreads.model.entities.Comment;
import com.it_talends_goodreads.goodreads.model.entities.Review;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.it_talends_goodreads.goodreads.model.repositories.CommentRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Transactional
    public CommentWithoutOwnerDTO create(CreateCommentDTO createCommentDTO, int userId) {
        User user = getUserById(userId);
        Comment comment;

        Review review = reviewRepository.findById(createCommentDTO.getReviewId()).orElseThrow(() -> new NotFoundException("No such review"));

        if (createCommentDTO.getParentId() != 0) {
            Optional<Comment> optionalParent = commentRepository.findById(createCommentDTO.getParentId());
            if (optionalParent.isEmpty()) {
                throw new NotFoundException("No such parent comment");
            }
            Comment parent = optionalParent.get();
            comment = Comment
                    .builder()
                    .writer(user)
                    .review(review)
                    .parent(parent)
                    .content(createCommentDTO.getContent())
                    .build();
        } else {
            comment = Comment
                    .builder()
                    .writer(user)
                    .review(review)
                    .parent(null)
                    .content(createCommentDTO.getContent())
                    .build();
        }
        commentRepository.save(comment);
        CommentWithoutOwnerDTO result = CommentWithoutOwnerDTO
                .builder()
                .id(comment.getId())
                .parentId(0)
                .writerName(comment.getWriter().getUserName())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .build();
        logger.info(String.format("User with id %d wrote comment with id", userId, result.getId()));
        return result;
    }

    @Transactional
    public CommentWithoutOwnerDTO update(CommentContentDTO commentContentDTO, int userId, int commentId) {

        Comment comment = exists(commentId);
        if (authorized(userId, comment)) {
            comment.setContent(commentContentDTO.getContent());
            commentRepository.save(comment);
            logger.info(String.format("User with id %d updated comment with id %d", userId, commentId));
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
        return commentRepository.findById(id).orElseThrow(() -> new BadRequestException("No such comment"));
    }

    private boolean authorized(int userId, Comment comment) {
        if (userId != comment.getWriter().getId()) {
            logger.warn(String.format("User with id %d is trying to update comment with id %d that does not belong to him",
                    userId, comment.getId()));
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }

    public void delete(int id, int userId) {
        Comment comment = exists(id);
        if (authorized(userId, comment)) {
            logger.info(String.format("User with id %d deleted comment with id %d",
                    userId, id));
            commentRepository.deleteById(id);
        }
    }

    public CommentPageDTO getAllByReview(int id, int pageN, int recordCount) {
        Pageable pageable = PageRequest.of(pageN, recordCount);
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("No such review"));
        Page<Comment> com = commentRepository.findAllByReview(review, pageable);
        int totalPages = com.getTotalPages();
        return CommentPageDTO
                .builder()
                .currentPage(pageN)
                .totalPages(totalPages)
                .comments(com
                        .map(c -> mapper.map(c, CommentWithoutOwnerDTO.class)))
                .build();
        
    }
}
