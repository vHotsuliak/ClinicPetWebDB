package com.store;

import com.models.Client;
import com.models.pets.Pet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
    synchronized public void add(Client client) {
        client.setId(0);
        addClient(client);
        client.setId(getClientLastID());
        client.getPet().setOwnerID(client.getId());
        client.getPet().setKindOfPet(client.getKindOfPet());
        addPet(client);
    }

    synchronized private void addClient(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    synchronized private void addPet(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, id);
            entityManager.remove(client.getPet());
            entityManager.remove(client);
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @Override
    public Client get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, id);
            Query query = entityManager.createQuery("select id from Pet p where  p.ownerID = (:ownerId)");
            client.setPet(entityManager.find(Pet.class, query.setParameter("ownerId", id).getResultList().get(0)));
            return client;
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @Override
    public void close() {
        this.entityManagerFactory.close();
    }

    @Override
    public Collection<Client> searchClient(String clientName, String petName, String kindOfPet) {
        return null;
    }

    public EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    private int getClientLastID() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            return  (int) entityManager.createQuery("select max(id) from  Client").getResultList().get(0);
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }
}
