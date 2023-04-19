package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {
    private int parentId;
    @NotNull
    private int reviewId;
    @Length(max=5000)
    private String content;
}
