package com.petgang.service.user;

import java.util.List;

import com.petgang.model.user.Gender;
import com.petgang.model.user.User;

public interface UserService {

	List<User> get();

	int register(String tel, String password);

	User get(int userId);
	
	int updateNickName(int userId,String nickName);
	
	int updateFaceUrl(int userId,String url);
	
	int updateGender(int userId,Gender gender);
	
}
