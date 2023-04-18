package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    private String firstName;
    private String lastName;
    @NonNull
    private String username;
    private String aboutMe;
    private String linkToSite;
    private String gender;
    private String profilePhoto;
}
