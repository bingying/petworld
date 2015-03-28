package com.petgang.dao.user;

import java.util.List;

import com.petgang.model.user.User;

public interface UserMapper {

    List<User> select();
}
