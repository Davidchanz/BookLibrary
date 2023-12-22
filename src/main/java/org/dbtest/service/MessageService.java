package org.dbtest.service;

import org.dbtest.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class MessageService {
    private SessionFactory factory = null;
    private static final MessageService service = new MessageService();
    private MessageService(){setup();}
    public static MessageService getInstance(){
        return service;
    }

    private void setup(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    public Message saveMessage(String text){
        Message message = new Message(text);
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(message);
            tx.commit();
        }
        return message;
    }

    public List<Message> getAllMessages(){
        List<Message> list;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            list = session.createQuery("from Message", Message.class).list();
            tx.commit();
        }
        for(Message m: list){
            System.out.println(m);
        }
        return list;
    }
    public void updateMessage(Long id, String text){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Message result = session.get(Message.class, id);
            //Query<Message> query = session.createQuery("from Message m where m.id=:id", Message.class);
            //query.setParameter("id", id);
            //Message result = query.uniqueResult();
            result.setText(text);
            tx.commit();
        }
    }
    public void deleteMessage(Long id){
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Message> query = session.createQuery("from Message m where m.id=:id", Message.class);
            query.setParameter("id", id);
            Message result = query.uniqueResult();
            session.remove(result);
            tx.commit();
        }
    }
}
