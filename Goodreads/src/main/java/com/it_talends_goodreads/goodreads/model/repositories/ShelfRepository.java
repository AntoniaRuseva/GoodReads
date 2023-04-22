package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.Shelf;
import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf,Integer> {
    List<Shelf> findAllByUserId(int userId);
    Optional<Shelf> findById(int id);
    Optional<Shelf> findByUserAndName(User user, String name);
    void deleteAllByUser(User userById);
}
