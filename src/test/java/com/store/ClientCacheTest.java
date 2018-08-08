package com.store;

import com.models.Client;
import com.models.pets.Cat;
import com.models.pets.Dog;
import com.models.pets.Pet;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientCacheTest {
    private final ClientCache CLIENT_CACHE = ClientCache.getInstance();


    @Test
    public void delete() {
        CLIENT_CACHE.add(new Client(0, "Bob", new Pet("Bars")));
        assertNotNull(CLIENT_CACHE.values());
        assertEquals(1, CLIENT_CACHE.values().size());
        CLIENT_CACHE.delete(0);
        assertNotNull(CLIENT_CACHE.values());
        assertEquals(0, CLIENT_CACHE.values().size());
    }

    @Test
    public void get() {
        CLIENT_CACHE.add(new Client(0, "Bob", new Pet("Bars")));
        assertNotNull(CLIENT_CACHE.values());
        assertEquals(new Client(0, "Bob", new Pet("Bars")), CLIENT_CACHE.get(0));
        CLIENT_CACHE.delete(0);
    }

    @Test
    public void edit() {
        CLIENT_CACHE.add(new Client(0, "Bob", new Pet("Bars")));
        CLIENT_CACHE.edit(new Client(0, "Bob", new Dog("Bars")));
        assertNotNull(CLIENT_CACHE.values());
        assertEquals(new Client(0, "Bob", new Dog("Bars")), CLIENT_CACHE.get(0));
        CLIENT_CACHE.delete(0);
    }

    @Test
    public void searchClient() {
        CLIENT_CACHE.add(new Client(0, "Bob", new Pet("Bars")));
        CLIENT_CACHE.add(new Client(1, "Фелик", new Cat("Lisa")));
        CLIENT_CACHE.add(new Client(2, "Gotsuliak", new Dog("Altay")));
        List<Client> clients = new LinkedList<>();
        clients.add(new Client(2, "Gotsuliak", new Dog("Altay")));
        assertEquals(new LinkedList<Client>(), CLIENT_CACHE.searchClient("HGHJ", "dsaf", ""));
        assertEquals(new LinkedList<Client>(), CLIENT_CACHE.searchClient("HGHJ", "", null));
        assertEquals(new LinkedList<Client>(), CLIENT_CACHE.searchClient("", "dsaf", "sdfhg"));
        assertEquals(clients, CLIENT_CACHE.searchClient("Gotsuliak", "", "Dog"));
        assertEquals(clients, CLIENT_CACHE.searchClient("", "Altay", "Dog"));
        assertEquals(clients, CLIENT_CACHE.searchClient("Gotsuliak", "Altay", "Dog"));
        clients.remove(0);
        clients.add(new Client(0, "Bob", new Pet("Bars")));
        assertEquals(clients, CLIENT_CACHE.searchClient("Bob", "Bars", "Pet"));
        assertEquals(clients, CLIENT_CACHE.searchClient("Bob", "Bars", ""));
        CLIENT_CACHE.delete(0);
        CLIENT_CACHE.delete(1);
        CLIENT_CACHE.delete(2);
    }

}