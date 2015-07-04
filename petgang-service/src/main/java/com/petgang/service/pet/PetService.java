package com.petgang.service.pet;

import java.util.Date;

import com.petgang.model.pet.Pet;

public interface PetService {

	int add(Pet pet);

	int updatePic(int id, int userId, String pic);

	int updateDescr(int id, int userId, String descr);

	int updateName(int id, int userId, String name);

	int updateBirthday(int id, int userId, Date date);

}
