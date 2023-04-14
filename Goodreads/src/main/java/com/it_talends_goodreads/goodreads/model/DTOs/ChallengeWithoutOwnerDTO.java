package com.it_talends_goodreads.goodreads.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.it_talends_goodreads.goodreads.model.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


public class ChallengeWithoutOwnerDTO {

    private int id;
    private int number;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateAdded;
}
