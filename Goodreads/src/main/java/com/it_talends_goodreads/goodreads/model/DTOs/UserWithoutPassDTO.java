package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithoutPassDTO {
    private int id;
    private String email;
    private String username;
    private String profilePhoto;
}
