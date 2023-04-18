package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @Email(message = "Invalid email")
    private String email;
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "To create a strong password, use at least 8 characters," +
                    " including uppercase and lowercase letters, at least one digit and one spacial character")
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    @Size(min = 1, max = 1000, message = "The size of this \"about me\" must be lower than 1000 character")
    private String aboutMe;
    @Pattern(regexp = "^((https?|ftp|smtp)://)?(www.)?[a-z0-9]+.[a-z]+(/[a-zA-Z0-9#]+/?)*$",
            message = "This is not a valid link")
    private String linkToSite;

    @Pattern(regexp = "[MF]", message = "Gender must be either M or F")
    private String gender;

}
