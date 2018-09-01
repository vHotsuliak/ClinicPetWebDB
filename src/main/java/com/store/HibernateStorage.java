package com.store;

import com.models.Client;
import com.models.pets.Pet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;

public class HibernateStorage implements Storage {
    private final EntityManagerFactory entityManagerFactory;

    public HibernateStorage() {
        entityManagerFactory = Persistence.createEntityManagerFactory("clinicHibernate");
    }

    @Override
    public Collection<Client> values() {
       EntityManager entityManager = this.entityManagerFactory.createEntityManager();
       entityManager.getTransaction().begin();
       try {
           Collection<Client> clients = entityManager.createQuery("from Client").getResultList();
           Collection<Pet> pets = entityManager.createQuery("from Pet ").getResultList();
           for (Client client : clients)
               for (Pet pet : pets)
                   if (client.getId() == pet.getOwnerID()) {
                       client.setPet(pet);
                       continue;
                   }
           return clients;
       }finally {
           entityManager.getTransaction().commit();
           entityManager.close();
       }
    }

    @Override
    public int getLastId() {
        return 0;
    }

    @Override
    public void add(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.persist(client.getPet());
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }

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
        this.entityManagerFactory.close();
    }

    @Override
    public Collection<Client> searchClient(String clientName, String petName, String kindOfPet) {
        return null;
    }
}
