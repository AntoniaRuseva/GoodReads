package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentContentDTO {
    @Length(max=5000)
    private String content;
}
