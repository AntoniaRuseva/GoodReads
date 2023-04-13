package com.it_talends_goodreads.goodreads.model.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "writer_id",nullable = false)
    private User writer;

    @Column(name = "likes")
    private Integer likes;
    @ManyToOne
    @JoinColumn(name = "review_id",nullable = false)
    private Review review;

}
