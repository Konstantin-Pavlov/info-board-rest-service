package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.mapper.MessageMapper;
import com.aston.infoBoardRestService.service.MessageService;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao = new MessageDao();
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    @Override
    public void saveMessage(MessageDto messageDTO) throws SQLException {
        Message message = messageMapper.toEntity(messageDTO);
        messageDao.saveMessage(message);
    }

    @Override
    public MessageDto getMessage(Long id) throws SQLException {
        Message message = messageDao.getMessage(id);
        if (message != null) {
            return messageMapper.toDTO(message);
        }
        return null;
    }

    @Override
    public List<MessageDto> getAllMessages() throws SQLException {
        List<Message> messages = messageDao.getAllMessages();
        return messageMapper.toDTOList(messages);
    }

    @Override
    public void updateMessage(MessageDto messageDTO) throws SQLException {
        Message message = messageMapper.toEntity(messageDTO);
        messageDao.updateMessage(message);
    }

    @Override
    public void deleteMessage(Long id) throws SQLException {
        messageDao.deleteMessage(id);
    }
}