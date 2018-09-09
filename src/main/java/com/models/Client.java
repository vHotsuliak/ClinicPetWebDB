package com.models;


import com.models.pets.Cat;
import com.models.pets.Dog;
import com.models.pets.Pet;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "user_id")
    private int id; // Contains id of this client

    @Column(name = "user_nic")
    private String clientName; // Contains client's name

    @Transient
    private Pet pet; // Contains pet of this client

    public Client() {
    }

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
        this.pet.setOwnerID(this.id);
        this.pet.setKindOfPet(pet instanceof Cat ? "Cat" :
                                    pet instanceof Dog ? "Dog" : "Pet");
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
        return this.pet instanceof Cat ? "Cat"
                    : this.pet instanceof Dog ? "Dog"
                        : "Pet";
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

    /**
     * Set id
     * @param id client id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set client's name
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * set pet for this client
     * @param pet
     */
    public void setPet(Pet pet) {
        this.pet = pet;
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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", pet=" + pet +
                '}';
    }
}
