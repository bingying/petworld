package com.petgang.dao.user;

import java.util.List;

import com.petgang.model.user.User;

public interface UserMapper {

	List<User> select();

	int insertNewUser(User user);

	User selectByUserId(int userId);

}
