package com.petgang.dao.pet;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.petgang.model.pet.PetCategory;

public interface PetCategoryMapper {

	List<PetCategory> select(@Param("offset") int offset,
			@Param("limit") int limit);

	List<PetCategory> selectByParent(@Param("parent") int parent,
			@Param("offset") int offset, @Param("limit") int limit);
}
