package com.petgang.service.user;

import java.util.List;

import com.petgang.model.user.User;

public interface UserService {

	List<User> get();

	int register(String tel, String password);

	User get(int userId);
}
