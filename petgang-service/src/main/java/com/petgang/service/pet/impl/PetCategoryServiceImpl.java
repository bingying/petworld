package com.petgang.service.pet.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.petgang.dao.pet.PetCategoryMapper;
import com.petgang.model.pet.PetCategory;
import com.petgang.service.pet.PetCategoryService;

public class PetCategoryServiceImpl implements PetCategoryService {

	@Autowired
	private PetCategoryMapper petCategoryMapper;
	@Override
	public List<PetCategory> getAll(int offset, int limit) {

		return petCategoryMapper.select(offset, limit);
	}

	@Override
	public List<PetCategory> getCategories(int parent, int offset, int limit) {

		return petCategoryMapper.selectByParent(parent, offset, limit);
	}

}
