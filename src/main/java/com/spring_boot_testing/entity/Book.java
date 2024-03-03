package com.spring_boot_testing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String title;
    @NotNull
    @Column(nullable = false)
    private String author;
    @NotNull
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(name = "published_year", nullable = false)
    private int publishedYear;
    @NotNull
    @Column(nullable = false)
    private String genre;
    @NotNull
    @Column(nullable = false)
    private BigDecimal price;
    @NotNull
    @Column(nullable = false)
    private Boolean available;

    @ElementCollection
    @CollectionTable(name = "book_ratings", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "rating")
    private List<Integer> ratings;

    public Double countAvgRating() {
        return this.getRatings().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publishedYear == book.publishedYear && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(isbn, book.isbn) && Objects.equals(genre, book.genre) && Objects.equals(price, book.price) && Objects.equals(available, book.available) && Objects.equals(ratings, book.ratings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn, publishedYear, genre, price, available, ratings);
    }
}