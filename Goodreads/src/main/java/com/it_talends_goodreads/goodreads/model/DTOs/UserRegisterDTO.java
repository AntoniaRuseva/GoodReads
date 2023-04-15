package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
}
