package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageDTO {
    List<ReturnReviewWithoutBookDTO> reviews;
    private int currentPage;
    private int totalPages;
}
