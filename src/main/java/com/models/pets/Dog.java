package com.models.pets;

/**
 * 
 * @author Vasyl Gotsuliak
 * @since 07.08.2017 
 */

public class Dog extends Pet {
	/**
	 * Creation of dog name
	 * @param dogName contains dog name
	 */
	public Dog(String dogName) {
		super(dogName);
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
		 * @return dog's name
		 */
	/*@Override
	String getName() {
		return super.getName();
	}

		/**
		 * Change name for this dog's name
		 * @param newName
		 */
		/*@Override
		void changeName(final String newName) {
			super.changeName(newName);
		}
 */