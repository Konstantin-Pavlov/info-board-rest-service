package com.aston.infoBoardRestService.service;

import com.aston.infoBoardRestService.dto.UserDto;

import java.sql.SQLException;

public interface UserService {
    void saveUser(UserDto user) throws SQLException;
    UserDto getUser(String name) throws SQLException;
}
