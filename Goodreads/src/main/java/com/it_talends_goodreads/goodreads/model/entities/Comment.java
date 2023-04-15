package com.it_talends_goodreads.goodreads.model.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
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

    @ManyToOne
    @JoinColumn(columnDefinition = "TEXT",nullable = false)
    private Review review;
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

}
