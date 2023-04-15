package com.it_talends_goodreads.goodreads.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDate;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailedInfoDTO {
    private String title;
    private String authorName;
    private Set<String> categories;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releasedDate;
    private String isbn;
    private Double rating;
    private String language;
    private String format;
    private int pages;
    private String description;
    private int rateCounter;
    private int reviewsCounter;
}
