package com.store;

import com.models.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Collection;

public class HibernateStorage implements Storage {
    private final SessionFactory factory;

    public HibernateStorage() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    @SuppressWarnings("JpaQlInspection")
    @Override
    public Collection<Client> values() {
        final Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            return session.createQuery("from Clients join Pets on Clients.user_id = Pets.client_id").list();
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public int getLastId() {
        return 0;
    }

    @Override
    public void add(Client client) {

    }

    @Override
    public void edit(Client client) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Client get(int id) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Collection<Client> searchClient(String clientName, String petName, String kindOfPet) {
        return null;
    }
}
