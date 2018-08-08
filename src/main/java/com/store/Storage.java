package com.store;

import com.models.Client;

import java.util.Collection;
import java.util.List;

public interface Storage {

     int getLastId();

     Collection<Client> values();

     void add(final Client client);

     void edit(final Client client);

     void delete(final int id);

     Client get(final int id);

     void close();

     Collection<Client> searchClient(String clientName, String petName, String kindOfPet);
}
