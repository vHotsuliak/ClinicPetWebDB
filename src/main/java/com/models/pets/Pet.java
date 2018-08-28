package com.models.pets;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pets")
public class Pet {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "pet_id")
	private int id;


	@Column(name = "client_id")
	private int ownerID;

	@Column(name = "kind_of_pet")
	private String kindOfPet;

	@Column(name = "nic")
	private String petName;


	public Pet() {
	}

	/**
	 * Creation of pet name.
	 * @param petName contains pet name.
	 */
	public Pet(String petName) {

		synchronized (this) {
			this.petName = petName;
		}
	}

	/**
	 * Return pet id.
	 * @return pet id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set pet id.
	 * @param id contains pet id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Return id of pet owner.
	 * @return id of pet owner.
	 */
	public int getOwnerID() {
		return ownerID;
	}

	/**
	 * Set id of pet owner.
	 * @param ownerID id of pet owner.
	 */
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 *
	 * @return kind of pet
	 */
	public String getKindOfPet() {
		return kindOfPet;
	}

	/**
	 * Set kind of pet.
	 * @param kindOfPet contains kind of pet.
	 */
	public void setKindOfPet(String kindOfPet) {
		this.kindOfPet = kindOfPet;
	}

	/**
	 * Set pet name.
	 * @param petName pet's name.
	 */
	public void setPetName(String petName) {
		this.petName = petName;
	}

	/**
	 * Return pet's name
	 * @return pet's name
	 */
	public String getPetName() {
		return petName;
	}

	@Override
	public String toString() {
		return "Pet{" +
				"id=" + id +
				", ownerID=" + ownerID +
				", kindOfPet='" + kindOfPet + '\'' +
				", petName='" + petName + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pet pet = (Pet) o;
		return Objects.equals(petName, pet.petName);
	}

	@Override
	public int hashCode() {

		return Objects.hash(petName);
	}
}
