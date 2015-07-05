package com.petgang.web.model.pet;

import java.util.Map;

import com.google.common.collect.ArrayListMultimap;

public class PetCategoryView {

	private ArrayListMultimap<Integer, Integer> groups;

	private Map<Integer, String> names;

	public ArrayListMultimap<Integer, Integer> getGroups() {
		return groups;
	}

	public void setGroups(ArrayListMultimap<Integer, Integer> groups) {
		this.groups = groups;
	}

	public Map<Integer, String> getNames() {
		return names;
	}

	public void setNames(Map<Integer, String> names) {
		this.names = names;
	}

}
