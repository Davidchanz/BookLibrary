package org.dbtest.service;

import org.dbtest.model.Book;
import org.dbtest.model.Shelf;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private SessionFactory factory = null;
    private static final BookService service = new BookService();
    private BookService(){setup();}
    public static BookService getInstance(){
        return service;
    }

    private void setup(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    public Book saveBook(Book book){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Shelf shelf = session.get(Shelf.class, book.getShelf().getId());
            book.setShelf(shelf);
            session.persist(book);
            shelf.getBooks().add(book);
            tx.commit();
        }
        return book;
    }

    public Book saveBook(String name, String url){
        Book book = new Book(name, url);
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(book);
            tx.commit();
        }
        return book;
    }

    public List<Book> getAllBooks(){
        List<Book> list;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            list = session.createQuery("from Book", Book.class).list();
            tx.commit();
        }
        for(Book m: list){
            System.out.println(m);
        }
        return list;
    }
    public void updateBook(Long id, String text){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Book result = session.get(Book.class, id);
            result.setName(text);
            tx.commit();
        }
    }
    public void updateBookCover(Long id, String cover){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Book result = session.get(Book.class, id);
            result.setCover(cover);
            tx.commit();
        }
    }
    public void deleteBook(Book book){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Book> query = session.createQuery("from Book m where m=:book", Book.class);
            query.setParameter("book", book);
            Book result = query.uniqueResult();
            session.remove(result);
            tx.commit();
        }
    }
    public Shelf saveShelf(String name, ArrayList<Book> books){
        Shelf shelf = new Shelf(name, books);
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(shelf);
            books.forEach((book)->{book.setShelf(shelf);});
            books.forEach(session::persist);
            tx.commit();
        }
        return shelf;
    }

    public List<Book> getShelfBooks(Shelf shelf){
        List<Book> list;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Book> query = session.createQuery("from Book m where m.shelf=:shelf", Book.class);
            query.setParameter("shelf", shelf);
            list = query.getResultList();

            tx.commit();
        }
        for(Book m: list){
            System.out.println(m);
        }
        return list;
    }

    public List<Book> getUncategorizedBooks(){
        List<Book> list;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            list = session.createQuery("from Book m where m.shelf is null", Book.class).list();
            tx.commit();
        }
        for(Book m: list){
            System.out.println(m);
        }
        return list;
    }

    public List<Shelf> getShelfs(){
        List<Shelf> list;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            list = session.createQuery("from Shelf", Shelf.class).list();
            tx.commit();
        }
        for(Shelf m: list){
            System.out.println(m);
        }
        return list;
    }

    public void deleteBookFromShelf(Book book){
        Shelf shelf = book.getShelf();
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Shelf> query = session.createQuery("from Shelf m where m=:shelf", Shelf.class);
            query.setParameter("shelf", shelf);
            Shelf result = query.uniqueResult();
            System.out.println("////////////////////////////befor");
            for(var m: result.getBooks()){
                System.out.println(m);
            }
            result.getBooks().remove(book);
            System.out.println("//////////////////////////////past");
            for(var m: result.getBooks()){
                System.out.println(m);
            }
            tx.commit();
        }
    }

    public void changeShelf(Shelf shelf, Book book) {
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            //Shelf resultShelf = session.get(Shelf.class, shelf.getId());
            Book resultBook = session.get(Book.class, book.getId());
            resultBook.setShelf(shelf);
            tx.commit();
        }
    }

    public void deleteShelf(Shelf shelf) {
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Shelf resultShelf = session.get(Shelf.class, shelf.getId());
            session.remove(resultShelf);
            tx.commit();
        }
    }

    public void updateShelf(Long id, String name){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Shelf resultShelf = session.get(Shelf.class, id);
            resultShelf.setName(name);
            tx.commit();
        }
    }
}
