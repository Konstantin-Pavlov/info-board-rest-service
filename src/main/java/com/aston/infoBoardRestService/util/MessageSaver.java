package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class MessageSaver {
    private final UserDao userDao = new UserDao();
    private final MessageDao messageDao = new MessageDao();
    private final Logger logger = Logger.getLogger(UserSaver.class.getName());

    public void generateAndSaveMessagesForUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            Message message = MessageGenerator.generateMessage(user.getId(), user.getName());
            messageDao.saveMessage(message);
            logger.info(String.format("Message %s saved for user %s", message.getContent(), user.getName()));
        }
    }
}