package com.store;

import com.models.Client;

import java.util.Collection;

public class UserCache implements Storage{
    private static final UserCache INSTANCE = new UserCache();



    private final Storage storage = new HibernateStorage();

    public static UserCache getInstance(){
        return INSTANCE;
    }


    @Override
    public int getLastId() {
        return this.storage.getLastId();
    }

    @Override
    public Collection<Client> values() {
        return this.storage.values();
    }

    @Override
    public void add(Client client) {
        this.storage.add(client);
    }

    @Override
    public void edit(Client client) {
        this.storage.edit(client);
    }

    @Override
    public void delete(int id) {
        this.storage.delete(id);
    }

    @Override
    public Client get(int id) {
        return this.storage.get(id);
    }

    @Override
    public Collection<Client> searchClient(String clientName, String petName, String kindOfPet) {
        return this.storage.searchClient(clientName, petName, kindOfPet);
    }

    @Override
    public void close() {
        this.storage.close();
    }
}
