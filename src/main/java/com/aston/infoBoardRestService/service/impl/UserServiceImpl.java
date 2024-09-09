package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.mapper.UserMapper;
import com.aston.infoBoardRestService.service.UserService;

import java.sql.SQLException;

import com.aston.infoBoardRestService.dao.UserDao;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDao();
    private final UserMapper userMapper = UserMapper.INSTANCE;
    @Override
    public void saveUser(UserDto userDto) throws SQLException {
        userDao.saveUser(userMapper.ToUser(userDto));
    }

    @Override
    public UserDto getUserByEmail(String email) throws SQLException {
        return userMapper.toUserDTO(userDao.getUserByEmail(email));
    }
}
