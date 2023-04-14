package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @ManyToMany
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "book")
    private List<BooksShelves> booksShelves;

    @Column(name = "released_date")
    private LocalDate releasedDate;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "language", nullable = false)
    private String language;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String format;

    @Column(name = "pages")
    private int pages;

    @Column(columnDefinition = "TEXT",name = "description")
    private String description;

    @Column(name = "cover_photo")
    private String coverPhoto;

    @Column(name = "rate_counter")
    private int rateCounter;
    @OneToMany(mappedBy = "book")
    private List<Review> reviews;
}