package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentContentDTO {

    @NonNull
    @Length(max = 2000)
    private String content;
}
