package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPageDTO {
    private List<BookCommonInfoDTO> books;
    private int currentPage;
    private int totalPages;
}
