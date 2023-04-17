package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.BooksShelves;
import com.it_talends_goodreads.goodreads.model.entities.Shelf;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksShelvesRepository extends JpaRepository<BooksShelves, Integer> {
    Optional<BooksShelves> findBooksShelvesByBookAndShelf(Book book, Shelf shelf);
    List<BooksShelves> getBooksShelvesByShelf_UserId(int ownerId);
    List<BooksShelves> getByBook_Id(int bookId);

}

