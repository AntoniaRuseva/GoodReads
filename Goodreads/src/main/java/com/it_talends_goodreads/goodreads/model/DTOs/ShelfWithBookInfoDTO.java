package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelfWithBookInfoDTO {
    private int id;
    private String name;
    private List<BookCommonInfoDTO> books;
}
