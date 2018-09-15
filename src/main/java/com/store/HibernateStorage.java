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
    synchronized public void add(final Client client) {
        client.setId(0);
        addClient(client);
        //it is necessary for the correct connection of the client and the pet
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
        if( !(clientName.isEmpty() && petName.isEmpty() && kindOfPet.isEmpty())) {
            clients = searchClientByName(clientName);
            List<Pet> pets = searchPet(kindOfPet, petName);
            for (int i = 0, size = clients.size(); i < size; i++ ) {
                for (Pet pet : pets) {
                    if (clients.get(i).getId() == pet.getOwnerID()) {
                        clients.get(i).setPet(pet);
                        continue;
                    }
                }
                if (clients.get(i).getPet() == null){
                    clients.remove(i);
                    i--; size--;
                }
            }
        }
        return clients;
    }

    /**
     * Searching pets by one or two of this parameters, if both of parameters are empty then return full list of pet.
     * @param kindOfPet kind of pet
     * @param petName pet name
     * @return requested list of pet if both parameters aren't empty, else full list of pet.
     */
    private List<Pet> searchPet(String kindOfPet, String petName) {
        return transaction(entityManager -> !kindOfPet.isEmpty() && petName.isEmpty() ?
                entityManager.createQuery("from Pet p where p.kindOfPet = :kindOfPet").setParameter("kindOfPet", kindOfPet).getResultList()
                : !petName.isEmpty() && kindOfPet.isEmpty() ?
                    entityManager.createQuery("from Pet p where p.petName = :petName").setParameter("petName", petName).getResultList()
                    : !(kindOfPet.isEmpty() && petName.isEmpty()) ?
                        entityManager.createQuery("from Pet p where p.kindOfPet = :kindOfPet and p.petName = :petName")
                            .setParameter("kindOfPet", kindOfPet).setParameter("petName", petName).getResultList()
                        : entityManager.createQuery("from Pet").getResultList());
    }

    /**
     * Searching clients by mame passed as parameter. If parameter is empty then return full list of clients.
     * @param clientName client name
     * @return requested list of clients. If parameter is empty then return full list of clients.
     */
    private List<Client> searchClientByName(String clientName) {
        return transaction(entityManager -> !clientName.isEmpty() ?
                entityManager.createQuery("from Client c where c.clientName = :clientName").setParameter("clientName", clientName).getResultList()
                : entityManager.createQuery("from Client").getResultList());
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
