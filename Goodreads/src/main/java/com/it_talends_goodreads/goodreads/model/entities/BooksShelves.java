package com.it_talends_goodreads.goodreads.model.entities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
}
