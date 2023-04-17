package com.it_talends_goodreads.goodreads.model.DTOs;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeProgressDTO {
    private int challengeId;
    private String userName;
    private int currentReadBook;
    private int target;
    private List<BookCommonInfoDTO> readBooksList;
    private int challengeYear;
}
