package com.it_talends_goodreads.goodreads.model.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "books_shelves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksShelves {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @Column(name = "date_added")
    private LocalDate dateAdded;
}
