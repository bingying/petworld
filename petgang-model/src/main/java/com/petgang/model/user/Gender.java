package com.petgang.model.user;

public enum Gender {

	MALE(0), FEMALE(1);

	int gender = 0;
	private Gender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return this.gender;
	}

	public static Gender getGender(int gender) {
		for (Gender tmp : Gender.values()) {
			if (tmp.getGender() == gender) {
				return tmp;
			}
		}
		return null;
	}

}
