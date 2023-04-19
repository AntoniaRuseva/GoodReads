package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCommonInfoDTO {
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String authorName;
}
