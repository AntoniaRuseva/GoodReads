package com.it_talends_goodreads.goodreads.model.DTOs;

import com.it_talends_goodreads.goodreads.model.entities.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentWithoutOwnerDTO {
    private int id;
    private int parentId;
    private String writerName;
    private int reviewId;
    private String content;
}
