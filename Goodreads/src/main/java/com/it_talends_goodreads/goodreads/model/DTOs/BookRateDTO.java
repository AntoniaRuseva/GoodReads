package com.it_talends_goodreads.goodreads.model.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRateDTO {
    @NotNull
    @Min(value = 1, message = "The number must be equals or greater than 1")
    @Max(value = 5, message = "The number must be equals or lower than 5")
    private int rating;
}
