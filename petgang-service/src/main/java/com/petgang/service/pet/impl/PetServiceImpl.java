package com.petgang.service.pet.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.petgang.dao.pet.PetMapper;
import com.petgang.model.pet.Pet;
import com.petgang.service.pet.PetService;

public class PetServiceImpl implements PetService {

	@Autowired
	private PetMapper petMapper;

	@Override
	public int add(Pet pet) {

		return petMapper.insert(pet);
	}

	@Override
	public int updatePic(int id, int userId, String pic) {

		return petMapper.updatePic(id, userId, pic);
	}

	@Override
	public int updateDescr(int id, int userId, String descr) {

		return petMapper.updateDescr(id, userId, descr);
	}

	@Override
	public int updateName(int id, int userId, String name) {

		return petMapper.updateName(id, userId, name);
	}

	@Override
	public int updateBirthday(int id, int userId, Date birthday) {

		
		return petMapper.updateBirthday(id, userId, birthday);
	}

}
