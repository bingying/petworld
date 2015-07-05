package com.petgang.service.pet.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.petgang.dao.pet.PetCategoryMapper;
import com.petgang.model.pet.PetCategory;
import com.petgang.service.pet.PetCategoryService;

public class PetCategoryServiceImpl
		implements
			PetCategoryService,
			InitializingBean {

	@Autowired
	private PetCategoryMapper petCategoryMapper;

	private ArrayListMultimap<Integer, Integer> categoryGroupMap = ArrayListMultimap
			.create();

	private Map<Integer, String> categoryMap = Maps.newHashMap();

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public List<PetCategory> getAll(int offset, int limit) {

		return petCategoryMapper.select(offset, limit);
	}

	@Override
	public List<PetCategory> getCategories(int parent, int offset, int limit) {

		return petCategoryMapper.selectByParent(parent, offset, limit);
	}

	@Override
	public void afterPropertiesSet() {
		try {
			List<PetCategory> categories = getAll(0, 1000);
			if (CollectionUtils.isNotEmpty(categories)) {
				for (PetCategory tmp : categories) {
					if (tmp.getParent() > 0) {
						categoryGroupMap.put(tmp.getParent(), tmp.getId());
					}
					categoryMap.put(tmp.getId(), tmp.getName());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public ArrayListMultimap<Integer, Integer> getCategoryGroupMap() {

		return categoryGroupMap;
	}

	@Override
	public Map<Integer, String> getCategoryMap() {

		return categoryMap;
	}

}
