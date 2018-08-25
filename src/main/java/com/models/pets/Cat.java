package com.models.pets;

public class Cat extends Pet {
	/**
	 *  Creation of cat name
	 * @param catName contains cat name
	 */
	public Cat(String catName) {
		super(catName);
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
/*

	/**
	 *
	 * @return cat's name
	 */
	/*@Override
	String getName() {
		return super.getName();
	}

	/**
	 * Change name for this cat's name
	 * @param newName
	 */
	/*@Override
	void changeName(final String newName) {
		super.changeName(newName);
	}
*/