package com.models;


import com.models.pets.Pet;

import java.util.Objects;

public class Client {
    private final int id; // Contains id of this client
    private final String clientName; // Contains client's name
    private final Pet pet; // Contains pet of this client

    /**
     * Creating client with id, name and pet which were passed as parameters.
     * @param id contains id for this client
     * @param clientName contains client's name
     * @param pet contains client's pet
     */
    public Client(final int id, final String clientName, final Pet pet) {
        this.id = id;
        this.clientName = clientName;
        this.pet = pet;
    }

    /**
     * Return id this client
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Return client's name
     * @return client's name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Return kind of pet this client
     * @return kind of pet
     */
    public String getKindOfPet() {
        String kind = "Pet";
        if (this.pet.getClass().toString().contains("Cat"))
            kind = "Cat";
        else  if (this.pet.getClass().toString().contains("Dog"))
            kind = "Dog";
        return kind;
    }


    /**
     * Return client's pet
     * @return pet
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Return Client's pet's name
     * @return pet's name
     */
    public String getPetName() {
        return pet.getPetName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(clientName, client.clientName) &&
                Objects.equals(pet, client.pet);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, clientName, pet);
    }
}
