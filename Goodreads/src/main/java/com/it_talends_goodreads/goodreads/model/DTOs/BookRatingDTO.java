package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRatingDTO {

    private String rating;
    private double counter;
}
