package com.it_talends_goodreads.goodreads.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "photo")
    private String photo;

    @Column(name = "born_in")
    private String bornIn;

    @Column(name = "born_on")
    private LocalDate bornOn;

    @Column(name = "biography")
    private String biography;

    @OneToMany(mappedBy = "author")
    private List<Book> books;
}
