package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;

import java.sql.SQLException;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.dto.UserDto;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDao();
    @Override
    public void saveUser(UserDto user) throws SQLException {
        userDao.saveUser(user);
    }

    @Override
    public UserDto getUser(String name) throws SQLException {
        return userDao.getUser(name);
    }
}
