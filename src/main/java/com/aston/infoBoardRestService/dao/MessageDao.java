package com.aston.infoBoardRestService.dao;

import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MessageDao {
    Logger logger = Logger.getLogger(UserDao.class.getName());


    public void saveMessage(Message message) throws SQLException {
        String query = "INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, message.getAuthorId());
            statement.setString(2, message.getContent());
            statement.setString(3, message.getAuthorName());
            statement.setObject(4, message.getTimestamp());
            statement.executeUpdate();
            logger.info("Message with author name " + message.getAuthorName() + " has been saved");
        }
    }

    public Message getMessage(Long id) throws SQLException {
        String query = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getMessage(resultSet);
            }
        }
        return null;
    }

    public List<Message> getMessagesByAuthorId(Long authorId) throws SQLException {
        String query = "SELECT * FROM messages WHERE author_id = ?";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = getMessage(resultSet);
                messages.add(message);
            }
        }
        return messages;
    }


    public List<Message> getAllMessages() throws SQLException {
        String query = "SELECT * FROM messages";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Message message = getMessage(resultSet);
                messages.add(message);
            }
        }
        return messages;
    }

    public void updateMessage(Message message) throws SQLException {
        String query = "UPDATE messages SET content = ?, author_id = ?, timestamp = ? WHERE id = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message.getContent());
            statement.setString(2, message.getContent());
            statement.setObject(3, message.getTimestamp());
            statement.setLong(4, message.getId());
            statement.executeUpdate();
        }
    }

    public void deleteMessage(Long id) throws SQLException {
        String query = "DELETE FROM messages WHERE id = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private static Message getMessage(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getLong("id"));
        message.setAuthorId(resultSet.getLong("author_id"));
        message.setContent(resultSet.getString("content"));
        message.setAuthorName(resultSet.getString("author_name"));
        message.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
        return message;
    }
}