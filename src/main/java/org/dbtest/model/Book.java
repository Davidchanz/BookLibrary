package org.dbtest.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.util.Objects;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "cover")
    private String cover;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Book() {
    }
    public Book(String name){
        this();
        setName(name);
        setUrl("src/main/resources/1.pdf");
    }
    public Book(String name, String url){
        this();
        setName(name);
        setUrl(url);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(url, book.url) && Objects.equals(cover, book.cover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, cover);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
