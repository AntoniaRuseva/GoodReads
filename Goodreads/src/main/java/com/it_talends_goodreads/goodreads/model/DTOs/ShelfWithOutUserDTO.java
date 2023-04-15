package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelfWithOutUserDTO {
    private int id;
    private String name;
    private int countBooksInTheShelve;
}
