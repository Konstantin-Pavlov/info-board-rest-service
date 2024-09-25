package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.mapper.MessageMapper;
import com.aston.infoBoardRestService.service.MessageService;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    private  MessageDao messageDao ;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    public MessageServiceImpl() {
        messageDao = new MessageDao();
    }

    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public boolean saveMessage(MessageDto messageDTO) throws SQLException {
        Message message = messageMapper.toMessage(messageDTO);
        return messageDao.saveMessage(message);
    }

    @Override
    public MessageDto getMessageById(Long id) throws SQLException {
        Message message = messageDao.getMessage(id);
        if (message != null) {
            return messageMapper.toMessageDto(message);
        }
        return null;
    }

    @Override
    public List<MessageDto> getAllMessages() throws SQLException {
        List<Message> messages = messageDao.getAllMessages();
        return messageMapper.toMessageDtoList(messages);
    }

    @Override
    public List<MessageDto> getMessagesByAuthorId(Long authorId) throws SQLException {
        List<Message> messagesByAuthorId = messageDao.getMessagesByAuthorId(authorId);
        return messageMapper.toMessageDtoList(messagesByAuthorId);
    }

    @Override
    public List<MessageDto> getMessagesByAuthorEmail(String email) throws SQLException {
        List<Message> messages = messageDao.getMessagesByAuthorEmail(email);
        return messageMapper.toMessageDtoList(messages);
    }

    @Override
    public void updateMessage(MessageDto messageDTO) throws SQLException {
        Message message = messageMapper.toMessage(messageDTO);
        messageDao.updateMessage(message);
    }

    @Override
    public boolean deleteMessage(Long id) throws SQLException {
        return messageDao.deleteMessage(id);
    }
}