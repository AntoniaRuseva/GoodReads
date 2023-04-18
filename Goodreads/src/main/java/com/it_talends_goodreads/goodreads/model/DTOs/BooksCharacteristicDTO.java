package com.it_talends_goodreads.goodreads.model.DTOs;

import com.it_talends_goodreads.goodreads.model.entities.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksCharacteristicDTO {
    private String authorName;
    private List<Category> categories;
    private LocalDate releasedDate;
    private Double rating;
    private String language;
    private String format;
    private int pages;

}
