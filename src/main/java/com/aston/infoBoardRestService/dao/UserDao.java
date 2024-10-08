package com.aston.infoBoardRestService.dao;

import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDao {
    Logger logger = Logger.getLogger(UserDao.class.getName());
    private final MessageDao messageDao = new MessageDao();

    public List<User> getAllUsers() throws SQLException {
        String query = """
                    SELECT u.id, u.username, u.email,
                           m.id AS message_id, m.author_id, m.content, m.author_name, m.timestamp
                    FROM users u
                    LEFT JOIN messages m ON u.id = m.author_id
                """;
        Map<Long, User> userMap = new HashMap<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long userId = resultSet.getLong("id");
                User user = userMap.get(userId);
                if (user == null) {
                    user = getUser(resultSet);
                    user.setMessages(new ArrayList<>());
                    userMap.put(userId, user);
                }
                Message message = getMessage(resultSet);
                if (message != null) {
                    user.getMessages().add(message);
                }
            }
        }
        return new ArrayList<>(userMap.values());
    }

    public Optional<User> getUserById(Long id) throws SQLException {
        String query = """
                    SELECT u.id, u.username, u.email,
                           m.id AS message_id, m.author_id, m.content, m.author_name, m.timestamp
                    FROM users u
                    LEFT JOIN messages m ON u.id = m.author_id
                    WHERE u.id = ?
                """;
        User user = null;
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (user == null) {
                    user = getUser(resultSet);
                    user.setMessages(new ArrayList<>());
                }
                Message message = getMessage(resultSet);
                if (message != null) {
                    user.getMessages().add(message);
                }
            }
        }
        if (user != null) {
            logger.info("User with id " + user.getId() + " has been found");
        } else {
            logger.warning("User with id " + id + " does not exist");
        }
        return Optional.ofNullable(user);
    }


    public Optional<User> getUserByEmail(String email) throws SQLException {
        String query = """
                    SELECT u.id, u.username, u.email,
                           m.id AS message_id, m.author_id, m.content, m.author_name, m.timestamp
                    FROM users u
                    LEFT JOIN messages m ON u.id = m.author_id
                    WHERE u.email = ?
                """;
        User user = null;
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (user == null) {
                    user = getUser(resultSet);
                    user.setMessages(new ArrayList<>());
                }
                Message message = getMessage(resultSet);
                if (message != null) {
                    user.getMessages().add(message);
                }
            }
        }
        if (user != null) {
            logger.info("User with email " + user.getEmail() + " has been found");
        } else {
            logger.warning("User with email " + email + " does not exist");
        }
        return Optional.ofNullable(user);
    }


    public boolean saveUser(User user) throws SQLException {
        if (getUserByEmail(user.getEmail()).isEmpty()) {
            String query = "INSERT INTO users (username, email) VALUES (?, ?)";
            try (Connection connection = DbUtil.getInstance().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.executeUpdate();
                logger.info("User with email " + user.getEmail() + " has been saved");
                if (user.getMessages() != null) {
                    User savedUser = getUserByEmail(user.getEmail()).orElseThrow(
                            () -> new UserNotFoundException("Can't find user with email: " + user.getEmail()));
                    for (Message message : user.getMessages()) {
                        message.setAuthorId(savedUser.getId());
                        messageDao.saveMessage(message);
                    }
                }
                return true;
            }
        } else {
            logger.warning("User with email " + user.getEmail() + " already exists");
            return false;
        }
    }

    // todo - fix checking if user not existing, now return void, catch exception and so on
    public boolean deleteUser(String email) throws SQLException {
        User user =  getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException("Can't find user with email: " + email));
        if (user == null) {
            logger.warning("User with email " + email + " does not exist");
            return false;
        }
        String query = "DELETE FROM users WHERE email = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getEmail());
            statement.executeUpdate();
            logger.info(String.format("User with id=[%d], email=[%s], name=[%s] has been deleted", user.getId(), user.getEmail(), user.getName()));
            if (user.getMessages() != null) {
                for (Message message : user.getMessages()) {
                    messageDao.deleteMessage(message.getId());
                    logger.info(String.format("%s has been deleted", message));
                }
            }
        }
        return true;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

    private Message getMessage(ResultSet resultSet) throws SQLException {
        Long messageId = resultSet.getLong("message_id");
        String userEmail = resultSet.getString("email");
        if (resultSet.wasNull()) {
            logger.warning(String.format(
                    "Message with id %s  and user email %s not found",
                    messageId, userEmail)
            );
            return null;
        }
        logger.info(String.format(
                "Message with id %s and user email %s found",
                messageId, userEmail)
        );
        Message message = new Message();
        message.setId(messageId);
        message.setAuthorId(resultSet.getLong("author_id"));
        message.setContent(resultSet.getString("content"));
        message.setAuthorName(resultSet.getString("author_name"));
        message.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
        return message;
    }

}
