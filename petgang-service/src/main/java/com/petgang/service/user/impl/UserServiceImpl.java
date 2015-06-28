package com.petgang.service.user.impl;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.petgang.commons.security.MD5;
import com.petgang.dao.user.UserMapper;
import com.petgang.model.user.Gender;
import com.petgang.model.user.User;
import com.petgang.service.user.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	private String DEFAULTFACEURL = "http://m3.app111.com/AppImg/160x160/2012/11/19/5555640821411058654414.jpg";

	@Override
	public List<User> get() {

		return userMapper.select();
	}

	@Override
	public int register(String tel, String password) {
		User user = new User();
		user.setTel(tel);
		user.setPassword(MD5.encode(password));
		user.setNickName(RandomNickName.getNickName());
		user.setFaceUrl(DEFAULTFACEURL);
		user.setPassport(tel);
		userMapper.insertNewUser(user);
		return user.getId();
	}

	@Override
	public User get(int userId) {

		return userMapper.selectByUserId(userId);
	}

	private static class RandomNickName {
		public static String getNickName() {
			StringBuilder builder = new StringBuilder();
			builder.append("弟子");
			builder.append(RandomStringUtils.random(5, false, true));
			return builder.toString();
		}
	}

	@Override
	public int updateNickName(int userId, String nickName) {
		
		return 0;
	}

	@Override
	public int updateFaceUrl(int userId, String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateGender(int userId, Gender gender) {
		// TODO Auto-generated method stub
		return 0;
	}
}
