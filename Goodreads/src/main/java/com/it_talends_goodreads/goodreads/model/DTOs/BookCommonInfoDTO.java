package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCommonInfoDTO {
    private int id;
    private String title;
    private String authorName;
}
