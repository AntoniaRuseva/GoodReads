package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFollowersDTO {
    private int id;
    private String email;
    private String username;
    private List<UserWithoutPassDTO> followers;
    private String profilePhoto;
}
