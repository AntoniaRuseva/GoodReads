package com.it_talends_goodreads.goodreads.model.DTOs;

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
    private String newPass;
    private String confirmNewPass;
}