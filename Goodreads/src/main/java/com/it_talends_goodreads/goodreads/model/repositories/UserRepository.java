package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
