package com.petgang.service.pet;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.petgang.model.pet.PetCategory;

public interface PetCategoryService {

	public List<PetCategory> getAll(int offset, int limit);

	public List<PetCategory> getCategories(int parent, int offset, int limit);
	
	public ArrayListMultimap<Integer, Integer> getCategoryGroupMap();
	
	public Map<Integer, String> getCategoryMap();
}
