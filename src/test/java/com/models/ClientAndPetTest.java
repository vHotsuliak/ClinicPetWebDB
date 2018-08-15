package com.models;

import com.models.pets.Cat;
import com.models.pets.Dog;
import com.models.pets.Pet;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;


public class ClientAndPetTest {
    private Client client;

    @Test
    public void clientGartersAndSetters() {
        client = new Client();
        assertNotNull(client);
        client.setId(1);
        assertEquals(1, client.getId());
        client.setClientName("Vasyl");
        assertEquals("Vasyl", client.getClientName());
        client.setPet(new Dog("Altay"));
        assertEquals(new Dog("Altay"), client.getPet());
    }

    @Test
    public void petGettersAndSetters() {
        Pet pet = new Pet();
        assertNotNull(pet);
        pet.setId(1);
        assertEquals(1, pet.getId());
        pet.setKindOfPet("Cat");
        assertEquals("Cat", pet.getKindOfPet());
        pet.setOwnerID(1);
        assertEquals(1, pet.getOwnerID());
        pet.setPetName("Lisa");
        assertEquals("Lisa", pet.getPetName());
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

    @SuppressWarnings({"SimplifiableJUnitAssertion", "ObjectEqualsNull", "ConstantConditions"})
    @Test
    public void equalsClassClient() {
        client = new Client(3, "Vasyl", new Pet("Altay"));
        assertEquals(client, new Client(3, "Vasyl", new Pet("Altay")));
        assertEquals(client, client);
        assertFalse(client.equals(null));
        client = new Client(4, "Vasyl", new Dog("Altay"));
        assertNotEquals(client, new Pet("Altay"));
        assertNotEquals(client, new Client(4, "Vasyl", new Pet("Altay")));
        client = new Client(5, "Julia", new Cat("Lisa"));
        assertNotEquals(client, new Client(5, "Julia", new Cat("Lis")));
        assertNotEquals(client, new Client(5, "Фелик", new Cat("Lis")));
        assertNotEquals(client, new Client(4, "Фелик", new Cat("Lis")));
    }

    @SuppressWarnings({"SimplifiableJUnitAssertion", "ObjectEqualsNull", "ConstantConditions"})
    @Test
    public void equalsClassPet() {
        Pet pet = new Pet("Lisa");
        assertEquals(pet, pet);
        assertFalse(pet.equals(null));
        assertNotEquals(pet, new Cat("Lisa"));
        pet = new Cat("Lisa");
        assertEquals(pet, new Cat("Lisa"));
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