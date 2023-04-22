package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
        Optional<Book> findByRateByd(User user);

}
