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
    @NonNull
    private int reviewId;
    @NonNull
    private String content;
}
