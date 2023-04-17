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

        @Query(value = "SELECT COUNT(*) FROM books JOIN books_shelves AS bs ON books.id = bs.book_id " +
                "JOIN shelves ON bs.shelf_id = shelves.id JOIN users ON users.id = shelves.user_id" +
                " WHERE users.id = ?1 AND shelves.name = 'Read' AND YEAR(bs.date_added) = ?2", nativeQuery = true)
        int findAllBooksForChallenge(int userId, int year);
}
