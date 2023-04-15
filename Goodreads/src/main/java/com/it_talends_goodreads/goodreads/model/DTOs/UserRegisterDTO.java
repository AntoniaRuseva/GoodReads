package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @Email(message = "Invalid email")
    private String email;
    private String username;
    @Pattern(regexp = "^(?=.\\d)(?=.[a-z])(?=.[A-Z])(?=.[!@#$%^&*()+,-./:;<=>?`{|}~]).{8,}$",
            message = "To create a strong password, use at least 8 characters," +
                    " including uppercase and lowercase letters, at least one digit, and one special symbol.")
    private String password;
    private String confirmPassword;
}
