package com.aston.infoBoardRestService.service;

import com.aston.infoBoardRestService.dto.MessageDto;

import java.sql.SQLException;
import java.util.List;

public interface MessageService {
    boolean saveMessage(MessageDto messageDTO) throws SQLException;

    MessageDto getMessageById(Long id) throws SQLException;

    List<MessageDto> getAllMessages() throws SQLException;

    List<MessageDto> getMessagesByAuthorId(Long authorId) throws SQLException;

    List<MessageDto> getMessagesByAuthorEmail(String email) throws SQLException;

    void updateMessage(MessageDto messageDTO) throws SQLException;

    boolean deleteMessage(Long id) throws SQLException;
}
