package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassDTO {
    private String currentPass;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "To create a strong password, use at least 8 characters," +
                    " including uppercase and lowercase letters, at least one digit and one spacial character")

    private String newPass;
    private String confirmNewPass;
}