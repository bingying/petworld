package com.petgang.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.petgang.model.user.User;

public interface UserMapper {

	List<User> select();

	int insertNewUser(User user);

	User selectByUserId(@Param("userId") int userId);

	int updateNickName(@Param("userId") int userId,
			@Param("nickName") String nickName);

	int updateFaceUrl(@Param("userId") int userId,
			@Param("faceUrl") String faceUrl);

	int updateGender(@Param("userId") int userId, @Param("gender") int gender);

}
