package org.dbtest.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Books_Authors_Inter")
public class BooksAuthorsInterseption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Book book_id;

    @ManyToOne
    private Author author_id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Author getAuthor_id() {
        return author_id;
    }

    public Book getBook_id() {
        return book_id;
    }

    public void setBook_id(Book book_id) {
        this.book_id = book_id;
    }

    public void setAuthor_id(Author author_id) {
        this.author_id = author_id;
    }

    public BooksAuthorsInterseption(){

    }
    public BooksAuthorsInterseption(Book book, Author author){
        this();
        setBook_id(book);
        setAuthor_id(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooksAuthorsInterseption that = (BooksAuthorsInterseption) o;
        return Objects.equals(id, that.id) && Objects.equals(book_id, that.book_id) && Objects.equals(author_id, that.author_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book_id, author_id);
    }
}
