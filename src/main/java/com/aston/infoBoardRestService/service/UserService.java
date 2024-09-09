package com.aston.infoBoardRestService.service;

import com.aston.infoBoardRestService.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void saveUser(UserDto user) throws SQLException;
    UserDto getUserByEmail(String name) throws SQLException;
    List<UserDto> getAllUsers() throws SQLException; // New method

}
