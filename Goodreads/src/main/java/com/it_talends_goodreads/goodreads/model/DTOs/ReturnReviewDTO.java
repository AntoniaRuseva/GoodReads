package com.it_talends_goodreads.goodreads.model.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnReviewDTO {
    private int id;
    private UserWithoutPassDTO writer;
    private BookCommonInfoDTO bookInfo;
    private int likes;
    private String content;
    private LocalDate date;


}
