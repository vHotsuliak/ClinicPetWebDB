package com.models.pets;

import java.util.Objects;

/**
 * 
 * @author Vasyl Gotsuliak
 * @since 07.08.2017 
 */

public class Pet {
	/*
	 * Save pet's name   
	 */
	private String petName;
	
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
	 * Return pet's name
	 * @return pet's name
	 */
	public String getPetName() {
		return petName;
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
