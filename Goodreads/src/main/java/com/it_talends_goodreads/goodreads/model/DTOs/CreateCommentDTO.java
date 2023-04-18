package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

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
