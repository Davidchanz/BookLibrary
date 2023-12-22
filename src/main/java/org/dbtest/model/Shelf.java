package org.dbtest.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Shelf")
public class Shelf {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(name = "name", nullable = false)
        private String name;

        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
        private Set<Book> books;

        public Shelf() {
            books = new HashSet<>();
        }

        public Shelf(String name, ArrayList<Book> books){
            this();
            setName(name);
            setBooks(books);
        }

    public Shelf(String name){
        this();
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return Objects.equals(id, shelf.id) && Objects.equals(name, shelf.name) && Objects.equals(books, shelf.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    public void setBooks(ArrayList<Book> books) {
        this.books.addAll(books);
    }

    public Set<Book> getBooks(){
            return books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
