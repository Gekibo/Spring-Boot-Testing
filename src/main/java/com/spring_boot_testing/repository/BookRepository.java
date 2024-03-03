package com.spring_boot_testing.repository;

import com.spring_boot_testing.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(String genre);

    List<Book> findByAuthorAndPublishedYear(String author, int publishedYear);

    int countBooksByPublishedYear(int publishedYear);

    List<Book> findByAuthor(String author);
@Query("SELECT AVG(r) FROM Book as b join b.ratings as r WHERE b.author = :author")
    Double calculateAverageRatingByAuthor(String author);
}
