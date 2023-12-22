package org.dbtest;

import org.dbtest.model.Author;
import org.dbtest.model.Book;
import org.dbtest.model.BooksAuthorsInterseption;
import org.dbtest.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PersistenceTest {
    private SessionFactory factory = null;

    @BeforeClass
    public void setup(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    public BooksAuthorsInterseption saveBook(Book book, Author author){
        BooksAuthorsInterseption message = new BooksAuthorsInterseption(book, author);
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(book);
            session.persist(author);
            session.persist(message);
            tx.commit();
        }
        return message;
    }

    @Test
    public void readMessage(){
        BooksAuthorsInterseption savedMessage = saveBook(new Book("Harry Potter"), new Author("Joan Rouling"));
        List<BooksAuthorsInterseption> list;
        Book book;
        Author author;
        try(Session session = factory.openSession()){
            list = session.createQuery("from BooksAuthorsInterseption", BooksAuthorsInterseption.class).list();
            Transaction tx = session.beginTransaction();
            Query<Book> query = session.createQuery("from Book b where b.id=:id", Book.class);
            query.setParameter("id", list.get(0).getBook_id().getId());
            book = query.uniqueResult();

            Query<Author> query1 = session.createQuery("from Author b where b.id=:id", Author.class);
            query1.setParameter("id", list.get(0).getAuthor_id().getId());
            author = query1.uniqueResult();
            tx.commit();
        }
        //assertEquals(list.size(), 1);
        System.out.println(book);
        System.out.println(author);

        //assertEquals(list.get(list.size()-1), savedMessage);
    }
}
