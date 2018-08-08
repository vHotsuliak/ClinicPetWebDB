package com.models;

import com.models.pets.Cat;
import com.models.pets.Dog;
import com.models.pets.Pet;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientAndPetTest {
    private Client client;

    @Test
    public void getId() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals(3, client.getId());
    }

    @Test
    public void getClientName() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals("Vasyl", client.getClientName());
    }

    @Test
    public void getKindOfPet() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals("Pet", client.getKindOfPet());
        client = new Client(4, "Vasyl", new Dog("Altay"));
        assertEquals("Dog", client.getKindOfPet());
        client = new Client(5, "Julia", new Cat("Lisa"));
        assertEquals("Cat", client.getKindOfPet());
    }

    @Test
    public void getPet() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals(new Pet("Altay"), client.getPet());
        client = new Client(4, "Vasyl", new Dog("Altay"));
        assertEquals(new Dog("Altay"), client.getPet());
        client = new Client(5, "Julia", new Cat("Lisa"));
        assertEquals(new Cat("Lisa"), client.getPet());
    }

    @Test
    public void getPetName() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals("Altay", client.getPetName());
        client = new Client(4, "Vasyl", new Dog("Altay"));
        assertEquals("Altay", client.getPetName());
        client = new Client(5, "Julia", new Cat("Lisa"));
        assertEquals("Lisa", client.getPetName());
    }

    @Test
    public void equalsClassClient() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals(true, client.equals(new Client(3, "Vasyl", new Pet("Altay"))));
        assertEquals(true, client.equals(client));
        assertEquals(false, client.equals(null));
        client = new Client(4, "Vasyl", new Dog("Altay"));
        assertEquals(false, client.equals(new Pet("Altay")));
        assertEquals(false, client.equals(new Client(4, "Vasyl", new Pet("Altay"))));
        client = new Client(5, "Julia", new Cat("Lisa"));
        assertEquals(false, client.equals(new Client(5, "Julia", new Cat("Lis"))));
        assertEquals(false, client.equals(new Client(5, "Фелик", new Cat("Lis"))));
        assertEquals(false, client.equals(new Client(4, "Фелик", new Cat("Lis"))));
    }

    @Test
    public void equalsClassPet() {
        Pet pet = new Pet("Lisa");
        assertTrue(pet.equals(pet));
        assertFalse(pet.equals(null));
        assertFalse( pet.equals(new Cat("Lisa")));
        pet = new Cat("Lisa");
        assertTrue( pet.equals(new Cat("Lisa")));
    }

    @Test
    public void hashCodeClassClient() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals(Objects.hash(3, "Vasyl", new Pet("Altay")), client.hashCode());
    }

    @Test
    public void hashCodeClassPet() {
        Pet pet = new Pet("Altay");
        assertEquals(Objects.hash("Altay"), pet.hashCode());
        pet = new Dog("Altay");
        assertEquals(Objects.hash("Altay"), pet.hashCode());
        pet = new Cat("Lisa");
        assertEquals(Objects.hash("Lisa"), pet.hashCode());
    }
}