package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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


    @ManyToMany(mappedBy = "ratedBooks", fetch = FetchType.LAZY)
    private Set<User> rateByd;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}