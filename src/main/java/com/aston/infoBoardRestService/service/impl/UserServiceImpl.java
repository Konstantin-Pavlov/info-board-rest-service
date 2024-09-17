package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.mapper.UserMapper;
import com.aston.infoBoardRestService.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDao();
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public boolean saveUser(UserDto userDto) throws SQLException {
        return userDao.saveUser(userMapper.ToUser(userDto));
    }

    @Override
    public UserDto getUserByEmail(String email) throws SQLException {
        return userMapper.toUserDTO(userDao.getUserByEmail(email));
    }

    @Override
    public List<UserDto> getAllUsers() throws SQLException {
        return userDao.getAllUsers()
                .stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) throws SQLException {
        User user = userDao.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException("Can't find user with userId: " + userId)
        );
        return userMapper.toUserDTO(user);
    }

    @Override
    public boolean deleteUser(String email) throws SQLException {
        return userDao.deleteUser(email);
    }
}
