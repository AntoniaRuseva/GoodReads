package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWithoutPassDTO {
    private int id;
    private String email;
    private String username;
}
