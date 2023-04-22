package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChallengeDTO {
    @NonNull
    @Min(value = 0, message = "The number must be positive")
    @Max(value = 1000, message = "These are too many books")
    private int number;

}
