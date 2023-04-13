package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "review")
    private List<Comment> comments;
}
