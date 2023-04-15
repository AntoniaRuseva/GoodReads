package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String aboutMe;
    private String linkToSite;
    private String gender;
}
