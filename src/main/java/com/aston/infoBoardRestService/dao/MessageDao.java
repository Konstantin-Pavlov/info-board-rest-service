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

public class MessageDao {

    public void saveMessage(Message message) throws SQLException {
        String query = "INSERT INTO messages (content, author_id, timestamp) VALUES (?, ?, ?)";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message.getContent());
            statement.setString(2, message.getAuthorName());
            statement.setObject(3, message.getTimestamp());
            statement.executeUpdate();
        }
    }

    public Message getMessage(Long id) throws SQLException {
        String query = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getLong("id"));
                message.setAuthorId(resultSet.getLong("authorId"));
                message.setContent(resultSet.getString("content"));
                message.setAuthorName(resultSet.getString("author"));
                message.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
                return message;
            }
        }
        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        String query = "SELECT * FROM messages";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getLong("id"));
                message.setAuthorId(resultSet.getLong("authorId"));
                message.setContent(resultSet.getString("content"));
                message.setAuthorName(resultSet.getString("author"));
                message.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
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
}