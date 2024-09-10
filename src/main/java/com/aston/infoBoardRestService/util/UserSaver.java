package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;

import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.IntStream;


public class UserSaver {

    private final UserService userService = new UserServiceImpl();
    private final UserDao userDao = new UserDao();
    private final Logger logger = Logger.getLogger(UserSaver.class.getName());

    public void generateAndSaveUsers(int numberOfUsers) {
        IntStream.rangeClosed(0, numberOfUsers).forEach(e -> {
            User user = UserGenerator.generateUser();
            try {
                userDao.saveUser(user);
            } catch (SQLException ex) {
                logger.severe(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
    }
}