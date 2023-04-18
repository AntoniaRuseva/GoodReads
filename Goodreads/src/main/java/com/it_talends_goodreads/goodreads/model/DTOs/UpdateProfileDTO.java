package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.it_talends_goodreads.goodreads.Util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    private String firstName;
    private String lastName;
    @NotNull
    private String username;
    @Size(min = 1, max = 1000, message = "The size of this \"about me\" must be lower than 1000 character")
    private String aboutMe;
    @Pattern(regexp = LINK_TO_SITE_REGEX,
            message = LINK_TO_SITE_MSG)
    private String linkToSite;
    @Pattern(regexp=GENDER_REGEX,
             message = GENDER_MSG)
    private String gender;
    private String profilePhoto;
}
