package com.store;

import com.models.Client;
import com.models.pets.Dog;
import com.models.pets.Pet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HibernateStorageTest {
    private final HibernateStorage hibernateStorage = new HibernateStorage();

    @Test
    public void addAndDelete() {
        this.hibernateStorage.add(new Client(0, "test", new Pet("test")));
        int lastId = hibernateStorage.getClientLastID();
        int size = hibernateStorage.values().size();
        assertEquals(size, this.hibernateStorage.values().size());
        this.hibernateStorage.delete(lastId);
        assertEquals(size - 1, this.hibernateStorage.values().size());
    }

    @Test
    public void get() {
        this.hibernateStorage.add(new Client(0, "test", new Pet("test")));
        int lastId = this.hibernateStorage.getClientLastID();
        int size = this.hibernateStorage.values().size();
        assertEquals(new Client(lastId, "test", new Pet("test")), this.hibernateStorage.get(lastId));
        this.hibernateStorage.delete(lastId);
        assertEquals(size - 1, this.hibernateStorage.values().size());
    }

    @Test
    public void edit() {
        this.hibernateStorage.add(new Client(0, "test", new Pet("test")));
        int lastId = this.hibernateStorage.getClientLastID();
        int size = this.hibernateStorage.values().size();
        this.hibernateStorage.edit(new Client(lastId, "test0", new Dog("test0")));
        assertEquals(new Client(lastId, "test0", new Dog("test0")), this.hibernateStorage.get(lastId));
        this.hibernateStorage.delete(lastId);
        assertEquals(size - 1, this.hibernateStorage.values().size());
    }

    @Test
    public void searchClient() {
        List<Client> clients = new ArrayList<>();
        this.hibernateStorage.add(new Client( 0, "searchClientTestValue", new Pet("searchClientTestValue")));
        int lastId = this.hibernateStorage.getClientLastID();
        int size = this.hibernateStorage.values().size();
        clients.add(new Client(lastId, "searchClientTestValue", new Pet("searchClientTestValue")));
        assertNotNull(this.hibernateStorage.searchClient("searchClientTestValue", "searchClientTestValue", "Pet"));
        assertNotNull( this.hibernateStorage.searchClient("", "searchClientTestValue", "Pet"));
        assertNotNull( this.hibernateStorage.searchClient("searchClientTestValue", "", "Pet"));
        assertNotNull( this.hibernateStorage.searchClient("searchClientTestValue", "searchClientTestValue", ""));
        assertNotNull( this.hibernateStorage.searchClient("searchClientTestValue", "", ""));
        assertNotNull( this.hibernateStorage.searchClient("", "searchClientTestValue", ""));
        assertNotEquals(0, this.hibernateStorage.searchClient("", "", "Pet").size());

        this.hibernateStorage.delete(lastId);
        assertEquals(size - 1, this.hibernateStorage.values().size());
    }

    @Test
    public void close() {
        this.hibernateStorage.close();
    }

}