package com.store;

import com.models.Client;
import com.models.pets.Pet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDBCStorageTest {
    private final JDBCStorage jdbcStorage = new JDBCStorage();

    @Test
    public void addAndDelete() {
        int size = this.jdbcStorage.values().size();
        int lastId = this.jdbcStorage.getClientLastID();
        this.jdbcStorage.add(new Client(lastId + 1, "test", new Pet("test")));
        assertEquals(size + 1, this.jdbcStorage.values().size());
        this.jdbcStorage.delete(lastId + 1);
        assertEquals(size, this.jdbcStorage.values().size());
    }

    @Test
    public void get() {
        int lastId = this.jdbcStorage.getClientLastID();
        int size = this.jdbcStorage.values().size();
        this.jdbcStorage.add(new Client(lastId + 1, "test", new Pet("test")));
        assertEquals(new Client(lastId + 1, "test", new Pet("test")), this.jdbcStorage.get(lastId + 1));
        this.jdbcStorage.delete(lastId + 1);
        assertEquals(size, this.jdbcStorage.values().size());
    }

    @Test
    public void edit() {
        int lastId = this.jdbcStorage.getClientLastID();
        int size = this.jdbcStorage.values().size();
        this.jdbcStorage.add(new Client(lastId + 1, "test", new Pet("test")));
        this.jdbcStorage.edit(new Client(lastId + 1, "test0", new Pet("test0")));
        assertEquals(new Client(lastId + 1, "test0", new Pet("test0")), this.jdbcStorage.get(lastId + 1));
        this.jdbcStorage.delete(lastId + 1);
        assertEquals(size, this.jdbcStorage.values().size());
    }

    @Test
    public void searchClient() {
        int lastId = this.jdbcStorage.getClientLastID();
        int size = this.jdbcStorage.values().size();
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(lastId + 1, "searchClientTestValue", new Pet("searchClientTestValue")));
        this.jdbcStorage.add(new Client(lastId + 1, "searchClientTestValue", new Pet("searchClientTestValue")));
        assertNotNull(this.jdbcStorage.searchClient("searchClientTestValue", "searchClientTestValue", "Pet"));
        assertNotNull( this.jdbcStorage.searchClient("", "searchClientTestValue", "Pet"));
        assertNotNull( this.jdbcStorage.searchClient("searchClientTestValue", "", "Pet"));
        assertNotNull( this.jdbcStorage.searchClient("searchClientTestValue", "searchClientTestValue", ""));
        assertNotNull( this.jdbcStorage.searchClient("searchClientTestValue", "", ""));
        assertNotNull( this.jdbcStorage.searchClient("", "searchClientTestValue", ""));
        assertNotEquals(0, this.jdbcStorage.searchClient("", "", "Pet").size());

        this.jdbcStorage.delete(lastId + 1);
        assertEquals(size, this.jdbcStorage.values().size());
    }

    @Test
    public void close() {
        this.jdbcStorage.close();
    }
}