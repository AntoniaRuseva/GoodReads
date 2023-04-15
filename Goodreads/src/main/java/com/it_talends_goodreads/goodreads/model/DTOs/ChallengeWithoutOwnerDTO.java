package com.it_talends_goodreads.goodreads.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.it_talends_goodreads.goodreads.model.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeWithoutOwnerDTO {

    private int id;
    private int number;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateAdded;
}
