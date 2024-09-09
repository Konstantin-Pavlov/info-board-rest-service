package com.aston.infoBoardRestService.dao;

import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDao {
    Logger logger = Logger.getLogger(UserDao.class.getName());

    public void saveUser(User user) throws SQLException {
        if (getUserByEmail(user.getEmail()) == null) {
            String query = "INSERT INTO users (username, email) VALUES (?, ?)";
            try (Connection connection = DbUtil.getInstance().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.executeUpdate();
                logger.info("User with email" + user.getEmail() + " has been saved");
            }
        } else {
            logger.warning("User with email " + user.getEmail() + " already exists");
        }

    }


    public User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setName(resultSet.getString("username"));
                logger.info("User with email " + user.getEmail() + " has been found");
                return user;
            } else {
                logger.warning("User with email " + email + " does not exist");
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);
            }
        }
        return users;
    }

    private static User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
}
