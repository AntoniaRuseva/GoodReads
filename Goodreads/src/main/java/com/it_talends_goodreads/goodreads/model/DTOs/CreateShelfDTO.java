package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateShelfDTO {
    @Length(max = 200, message = "The name is too long")
    private String name;
}
