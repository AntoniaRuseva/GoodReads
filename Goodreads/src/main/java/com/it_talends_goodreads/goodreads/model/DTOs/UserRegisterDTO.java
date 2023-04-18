package com.it_talends_goodreads.goodreads.model.DTOs;

import com.it_talends_goodreads.goodreads.model.entities.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.it_talends_goodreads.goodreads.Util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @Email(message = "Invalid email")
    private String email;
    @NotNull
    private String username;

    @Pattern(regexp = PASS_REGEX,
            message = STRONG_PASS_MSG)
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    @Size(min = 1, max = 1000, message = "The size of this \"about me\" must be lower than 1000 character")
    private String aboutMe;
    @Pattern(regexp = LINK_TO_SITE_REGEX,
            message = LINK_TO_SITE_MSG)
    private String linkToSite;

    @Pattern(regexp = GENDER_REGEX, message = GENDER_MSG)
    private String gender;

}
