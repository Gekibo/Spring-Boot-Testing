package com.spring_boot_testing.repository;

import com.spring_boot_testing.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    private Book book;
    
    @BeforeEach
    void setUp(){
        book = new Book(
                1L,
                "Władca Pierścieni",
                "J.R.R Tolkien",
                "1234567AB",
                1950,
                "Fantasy",
                new BigDecimal("49.99"),
                true,
                Arrays.asList(5,4,9,3)
        );
    }
    @DisplayName("Test zapisywania książki")
    @Test
    void testSavedBook(){
        //given

        //when
        Book savedBook = bookRepository.save(book);
        //then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isGreaterThan(0);
        assertThat(savedBook).isEqualTo(book);

    }
    @DisplayName("Pobieranie wszystkich książek")
    @Test
    void givenBookList_whenFindAll_thenReturnBookList(){
        //given
        Book book2 = new Book(
                2L,
                "Hary Potter",
                "J.K. Rowling",
                "12345678ABC",
                1997,
                "Fantasy",
                new BigDecimal("49.99"),
                true,
                Arrays.asList(7,5,2,3)
        );
        bookRepository.save(book);
        bookRepository.save(book2);

        //when
        List<Book> bookList = bookRepository.findAll();
        //then
        assertThat(bookList).isNotNull();
        assertThat(bookList.size()).isEqualTo(2);

    }
    @DisplayName("Pobieranie książki po ID")
    @Test
    void givenBookObject_whenFindById_thenReturnEmployee(){
        //given
        bookRepository.save(book);
        //when
        Book bookById = bookRepository.findById(book.getId()).get();
        //then
        assertThat(bookById).isNotNull();
        assertThat(bookById).isEqualTo(book);

    }
    @DisplayName("Uaktualnianie książki")
    @Test
    void givenBookWithChangedFieldsWhenUpdateBook_thenReturnUpdatedBook(){
        //given
        bookRepository.save(book);
        Book savedBook = bookRepository.findById(book.getId()).get();
        savedBook.setAuthor("Brzęczyszczykiewicz");
        savedBook.setGenre("Horror");
        //when
        Book updatedBook = bookRepository.save(savedBook);
        //then
        assertThat(updatedBook.getAuthor()).isEqualTo("Brzęczyszczykiewicz");
        assertThat(updatedBook.getGenre()).isEqualTo("Horror");
    }
    @DisplayName("Usuwanie kisiążki")
    @Test
    void givenBook_whenDelete_thenBookNotPresent(){
        //given
        bookRepository.save(book);
        //when
        bookRepository.deleteById(book.getId());
        Optional<Book> bookOptional = bookRepository.findById(book.getId());
        //then
        assertThat(bookOptional).isEmpty();
    }
    @DisplayName("Wyszukiwanie książek według gatunku")
    @Test
    void givenBook_whenFindByGenre_thenReturnBook(){
        //given
        String genre = bookRepository.save(book).getGenre();
        //when
        List<Book> foundBooks =bookRepository.findByGenre(genre);
        //then
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks.get(0).getGenre()).isEqualTo(genre);
    }
    @DisplayName("Wyszukiwania książek po autorze i roku publikacji")
    @Test
    void testFindBookByAuthorAndPublicationYear(){
        //given
        Book savedBook = bookRepository.save(book);
        //when
        List<Book> foundBooks =bookRepository.findByAuthorAndPublishedYear(savedBook.getAuthor(), savedBook.getPublishedYear());

        //then
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks).allMatch((b)->b.getAuthor().equals(savedBook.getAuthor())&& b.getPublishedYear()== savedBook.getPublishedYear());

    }
    @DisplayName("Policzenia książek po roku publikacji")
    @Test
    void testCountBooksByPublicationYear(){
        //given
        bookRepository.save(book);
        //when
        int count = bookRepository.countBooksByPublishedYear(book.getPublishedYear());

        //then
        assertThat(count).isEqualTo(1);
    }
    @DisplayName("obliczania średniej oceny dla autora")
    @Test
    void testCalculateAverageRatingByAuthor(){
        //given
        Book newBook1 = new Book(2L, "Hary Potter", "J.K. Rowling", "12345678A", 1997, "Fantasy", new BigDecimal("49.99"), true, Arrays.asList(7,5,2,3));
        Book newBook2 = new Book(3L, "Hary Potter", "J.K. Rowling", "12345678C", 1997, "Fantasy", new BigDecimal("49.99"), true, Arrays.asList(5,5,5,1));
        Book newBook3 = new Book(4L, "Hary Potter", "J.K. Rowling", "12345678D", 1997, "Fantasy", new BigDecimal("49.99"), true, Arrays.asList(4,3,3,8));

        bookRepository.save(book);
        bookRepository.save(newBook1);
        bookRepository.save(newBook2);
        bookRepository.save(newBook3);

        List<Book> bookList = bookRepository.findByAuthor(book.getAuthor());
        Double avgRating = bookRepository.calculateAverageRatingByAuthor(book.getAuthor());

        //then
        assertThat(avgRating).isNotNull();
        assertThat(avgRating).isEqualTo(bookList
                .stream()
                .mapToDouble(Book::countAvgRating)
                .average()
                .orElse(0.0));
    }

}