package com.it_talends_goodreads.goodreads.model.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "date_added", nullable = false)

    private LocalDate dateAdded;

}
