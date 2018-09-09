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
    public void add(final Client client) {
        client.setId(0);
        addClient(client);
        //it's need to connect correctly client and pet
        client.getPet().setOwnerID(getClientLastID());
        addPet(client);
    }

    synchronized private void addClient(final Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
        }finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    synchronized private void addPet(final Client client) {
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
    public void edit(final Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            //without this, a pet has default id and because will be created new pet in table
            Client client1 = this.get(client.getId());
            client.getPet().setId(client1.getId());
            //---------------------------------------
            entityManager.getTransaction().begin();
            entityManager.merge(client.getPet());
            entityManager.merge(client);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @Override
    public void delete(final int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client client = this.get(id);
            Pet pet = client.getPet();
            entityManager.remove(entityManager.contains(pet) ? pet : entityManager.merge(pet));
            entityManager.remove(entityManager.contains(client) ? client : entityManager.merge(client));
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @Override
    public Client get(final int id) {
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

    @Override
    public int getClientLastID() {
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
