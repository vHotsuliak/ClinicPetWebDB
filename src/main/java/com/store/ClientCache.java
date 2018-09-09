package com.store;
//SingleTone
import com.models.Client;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCache implements Storage{
    private static final ClientCache INSTANCE = new ClientCache();
    private final ConcurrentHashMap<Integer, Client> clients = new ConcurrentHashMap<>();

    public static ClientCache getInstance() {
        return INSTANCE;
    }

    // Needed for JDBC
    @Override
    public int getClientLastID() {
        return 0;
    }

    public Collection<Client> values() {
        return this.clients.values();
    }

    /**
     * Create client for these clients
     * @param client client for adding
     */
    public void add(final Client client) {
        this.clients.put(client.getId(), client);
    }

    /**
     * Changing some details for client which identification by using id. Id cannot be change.
     *  Available changes - client's name or pet's name or kind of pet.
     * @param client client with changes(name or pet's name or kind of pet)
     */
    public void edit(final Client client) {
        this.clients.replace(client.getId(), client);
    }

    /**
     * Delete client by id.
     * @param id id of client
     */
    public void delete(final int id) {
        this.clients.remove(id);
    }

    /**
     * Return client by id.
     * @param id client's id
     * @return clint
     */
    public Client get(final int id) {
        return this.clients.get(id);
    }

    // Needed for JDBC
    @Override
    public void close() {

    }

    /**
     * Searching clients by  parameters. If some of parameters wasn't passed then it count as true fo each clients.
     * @param clientName client's name for searching
     * @param petName pet's name for searching
     * @param kindOfPet kind of pet
     * @return list of clients
     */
    public List<Client> searchClient(String clientName, String petName, String kindOfPet){
        List<Client> clients = new LinkedList<>();
        Check check = new Check();
        for (Map.Entry<Integer, Client> client: this.clients.entrySet()){
            if (check.checkClient(client.getValue(), clientName, petName, kindOfPet))
                clients.add(client.getValue());
        }
        return clients;
    }




    private class Check {
        /**
         * Check clientName, petName, kindOfPet is the same in client or not.
         * Return true if client has the same clientName, petName, kindOfPet or Expected date was missed else false
         * @param client clint for checking
         * @param clientName expected client's name
         * @param petName expected pet's name
         * @param kindOfPet expected kind of pet
         * @return true if client has the same clientName, petName, kindOfPet or Expected date was missed else false
         */
        private boolean checkClient(Client client, String clientName, String petName, String kindOfPet) {
            return checkClientName(client.getClientName(), clientName) && checkPetName(client.getPetName(), petName) && checkKindOfPet(client.getKindOfPet(), kindOfPet);
        }


        /**
         * Return true if realKindOfPet and expectKindOfPet are the same or was missed else false
         * @param realKindOfPet real kind of pet
         * @param expectKindOfPet expected kind of pet
         * @return true if realKindOfPet and expectKindOfPet are the same or was missed else false
         */
        private boolean checkKindOfPet(String realKindOfPet, String expectKindOfPet) {
            return expectKindOfPet.equals("") || realKindOfPet.equalsIgnoreCase(expectKindOfPet);
        }

        /**
         * Return true if realPetName and expectPetName are the same or was missed else false
         * @param realPetName real pet's name
         * @param expectPetName expected pet's name
         * @return true if realPetName and expectPetName are the same or was missed else false
         */
        private boolean checkPetName(String realPetName, String expectPetName) {
            return expectPetName.equals("") || realPetName.equalsIgnoreCase(expectPetName);
        }

        /**
         * Return true if realClientName and expectClientName are the same or was missed else false
         * @param realClientName real client's name
         * @param expectClientName expected client's name
         * @return true if realClientName and expectClientName are the same or was missed else false
         */
        private boolean checkClientName(String realClientName, String expectClientName) {
            return expectClientName.equals("") || realClientName.equalsIgnoreCase(expectClientName);
        }
    }


}
