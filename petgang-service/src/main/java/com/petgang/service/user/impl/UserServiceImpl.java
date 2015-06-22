package com.petgang.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.petgang.dao.user.UserMapper;
import com.petgang.model.user.User;
import com.petgang.service.user.UserService;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> get() {

        return userMapper.select();
    }

}
