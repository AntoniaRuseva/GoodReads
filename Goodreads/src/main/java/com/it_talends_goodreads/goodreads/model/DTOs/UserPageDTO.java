package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;
import org.springframework.data.domain.Page;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDTO {
    Page<UserWithoutPassDTO> users;
    private int currentPage;
    private int totalPages;
}
