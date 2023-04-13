package com.it_talends_goodreads.goodreads.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "user",nullable = false)
    private User user;

    @Column(name = "number",nullable = false)
    private String number;
    @Column(name = "date_added",nullable = false)
    private LocalDate dateAdd;
}
