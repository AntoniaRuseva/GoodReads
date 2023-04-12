package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToMany
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;


    @OneToMany(mappedBy = "book")
    private List<BooksShelves> booksShelves;

    @Column(name = "released_date")
    private LocalDate releasedDate;

    @Column(name = "ISBN")
    private int isbn;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "language")
    private String language;

    @Column(name = "format")
    private String format;

    @Column(name = "pages")
    private int pages;

    @Column(name = "description")
    private String description;

    @Column(name = "cover_photo")
    private String coverPhoto;

    @Column(name = "rate_counter")
    private int rateCounter;
}