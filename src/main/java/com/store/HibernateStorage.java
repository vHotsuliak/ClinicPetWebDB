package com.store;

import com.models.Client;
import com.models.pets.Pet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HibernateStorage implements Storage {
    private final EntityManagerFactory entityManagerFactory;

    HibernateStorage() {
        entityManagerFactory = Persistence.createEntityManagerFactory("clinicHibernate");
    }

    /**
     * Interface for  lambda excretion
     * @param <T> generic type
     */
    public interface Command<T> {
        T process(EntityManager entityManager);
    }


    private <T> T transaction(Command<T> command) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            return command.process(entityManager);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }
    /**
     * Return list of all clients.
     * @return list of all clients.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Client> values() {
       return transaction((EntityManager entityManager) -> {
               Collection<Client> clients = entityManager.createQuery("from Client").getResultList();
               Collection<Pet> pets = entityManager.createQuery("from Pet ").getResultList();
               for (Client client : clients)
                   for (Pet pet : pets)
                       if (client.getId() == pet.getOwnerID()) {
                           client.setPet(pet);
                           continue;
                       }
               return clients;});
    }


    /**
     * Add client and his pet to tables.
     * @param client contains client
     */
    @Override
    public void add(final Client client) {
        client.setId(0);
        addClient(client);
        //it's need to connect correctly client and pet
        client.getPet().setOwnerID(getClientLastID());
        addPet(client);
    }

    /**
     * Add the client to clients table.
     * @param client contains client
     */
    synchronized private void addClient(final Client client) {
        transaction(entityManager -> {
            entityManager.persist(client);
            return null;
        });
    }

    /**
     * Add the pet to pets table.
     * @param client contains client
     */
    synchronized private void addPet(final Client client) {
        transaction(entityManager -> {
            entityManager.persist(client.getPet());
            return null;
        });
    }

    /**
     * Editing client data in database, also editing pet data in database. Id won't be updated.
     * @param client contains client data include pet.
     */
    @Override
    public void edit(final Client client) {
        transaction(entityManager -> {
            //without this, a pet has default id and because will be created new pet in table
            Client client1 = this.get(client.getId());
            client.getPet().setId(client1.getPet().getId());
            client.getPet().setKindOfPet(client.getKindOfPet());
            //---------------------------------------
            // Without this cannot change kind of pet. Without this will create new pet in database and old will stay in the database
            Pet pet = client1.getPet();
            entityManager.remove(entityManager.contains(pet) ? pet : entityManager.merge(pet));
            //----------------------------------------------------------------------------------
            entityManager.merge(client.getPet());
            entityManager.merge(client);
            return null;
        });
    }

    /**
     * Deleting client and his pet.
     * @param id client id
     */
    @Override
    public void delete(final int id) {
        transaction(entityManager -> {
            Client client = this.get(id);
            Pet pet = client.getPet();
            entityManager.remove(entityManager.contains(pet) ? pet : entityManager.merge(pet));
            entityManager.remove(entityManager.contains(client) ? client : entityManager.merge(client));
            return null;
        });
    }

    /**
     * Get client by his id.
     * @param id client id.
     * @return client which have this id. If client with this id isn't exist then return null.
     */
    @Override
    public Client get(final int id) {
        return transaction(entityManager -> {
            Client client = entityManager.find(Client.class, id);
            Query query = entityManager.createQuery("select id from Pet p where  p.ownerID = (:ownerId)");
            client.setPet(entityManager.find(Pet.class, query.setParameter("ownerId", id).getResultList().get(0)));
            return client;
        });
    }


    /**
     * Choosing way to searching clients. Return list of clients with these parameters.
     * @param clientName contains client's name.
     * @param petName contains pet's name.
     * @param kindOfPet contains kind of pet
     * @return list of clients with these parameters.
     */
    @Override
    public Collection<Client> searchClient(final String clientName, final String petName, final String kindOfPet) {
        List<Client> clients = new ArrayList<>();

        return clients;
    }


    /**
     * Searching clients by one parameter.
     * @param condition1 1st condition.
     * @param SQLSearchRequest contains SQL search request with one parameter.
     * @return list of clients with this parameter.
     */
    private List<Client> oneCondition(final String condition1, final String SQLSearchRequest) {
        final List<Client> clients = new ArrayList<>();

        return clients;
    }

    /**
     * Searching clients by two parameters.
     * @param condition1 1st condition.
     * @param condition2 2nd condition.
     * @param SQLSearchRequest contains SQL search request with two parameters.
     * @return list of clients with these parameters.
     */
    private List<Client> twoCondition(String condition1, String condition2, String SQLSearchRequest) {
        final List<Client> clients = new ArrayList<>();

        return clients;
    }

    /**
     * Searching clients by three parameters.
     * @param condition1 1st condition.
     * @param condition2 2nd condition.
     * @param condition3 3rd condition.
     * @return list of clients with these parameters.
     */
    private List<Client> threeCondition(String condition1, String condition2, String condition3) {
        final List<Client> clients = new ArrayList<>();

        return clients;
    }

    /**
     * Return last client id.
     * @return last client id.
     */
    @Override
    public int getClientLastID() {
        return transaction(entityManager -> (int) entityManager.createQuery("select max(id) from  Client").getResultList().get(0));
    }

    /**
     * Close connection to database.
     */
    @Override
    public void close() {
        this.entityManagerFactory.close();
    }
}
