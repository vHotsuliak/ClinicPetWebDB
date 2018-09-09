package com.models.pets;

import javax.persistence.Entity;

@Entity
public class Cat extends Pet {
	/**
	 *  Creation of cat name
	 * @param catName contains cat name
	 */
	public Cat(String catName) {
		super(catName);
	}

    public Cat() {
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
