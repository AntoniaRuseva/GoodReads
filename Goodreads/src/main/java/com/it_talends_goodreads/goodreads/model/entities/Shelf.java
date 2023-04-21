package com.it_talends_goodreads.goodreads.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shelves")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy ="shelf")
    private Set<BooksShelves> booksShelves;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return id == shelf.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
