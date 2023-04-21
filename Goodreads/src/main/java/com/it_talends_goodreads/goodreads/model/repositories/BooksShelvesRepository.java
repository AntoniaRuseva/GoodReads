package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.BooksShelves;
import com.it_talends_goodreads.goodreads.model.entities.Shelf;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksShelvesRepository extends JpaRepository<BooksShelves, Integer> {
    Optional<BooksShelves> findBooksShelvesByBookAndShelf(Book book, Shelf shelf);
    Page<BooksShelves> getBooksShelvesByShelf_UserId(int ownerId, Pageable pageable);
    List<BooksShelves> getByBook_Id(int bookId);
    @Query(value = "SELECT COUNT(*) FROM goodreads.books_shelves WHERE shelf_id = ?1 AND book_id = ?2", nativeQuery = true)
    int existBooksShelvesByBookAndShelf(int shelfId,int  bookId);

}

