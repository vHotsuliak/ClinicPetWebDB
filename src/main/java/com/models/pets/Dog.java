package com.models.pets;

import javax.persistence.Entity;

@Entity
public class Dog extends Pet {
	/**
	 * Creation of dog name
	 * @param dogName contains dog name
	 */
	public Dog(String dogName) {
		super(dogName);
	}

	public Dog() {
		super();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
