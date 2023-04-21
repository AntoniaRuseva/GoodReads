package com.it_talends_goodreads.goodreads.model.entities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "books_shelves")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BooksShelves{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;


    @ManyToOne
    @JoinColumn(name = "shelf_id",nullable = false)
    private Shelf shelf;

    @Column(name = "date_added",nullable = false)
    private LocalDate dateAdded;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooksShelves that = (BooksShelves) o;
        return Objects.equals(book, that.book) && Objects.equals(shelf, that.shelf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, shelf);
    }
}
