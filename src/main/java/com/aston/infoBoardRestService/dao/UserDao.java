package com.aston.infoBoardRestService.dao;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void saveUser(UserDto user) throws SQLException {
        String query = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.executeUpdate();
        }
    }


    public UserDto getUser(String name) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserDto user = new UserDto();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("surname"));
                return user;
            }
        }
        return null;
    }

}
