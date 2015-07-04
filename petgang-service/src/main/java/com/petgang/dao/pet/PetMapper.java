package com.petgang.dao.pet;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.petgang.model.pet.Pet;

public interface PetMapper {

	int insert(Pet pet);

	List<Pet> selectByUserId(@Param("userId") int userId);

	int updatePic(@Param("id") int id, @Param("userId") int userId,
			@Param("pic") String pic);

	int updateDescr(@Param("id") int id, @Param("userId") int userId,
			@Param("descr") String descr);

	int updateName(@Param("id") int id, @Param("userId") int userId,
			@Param("name") String name);

	int updateBirthday(@Param("id") int id, @Param("userId") int userId,
			@Param("birthday") Date birthday);

}
