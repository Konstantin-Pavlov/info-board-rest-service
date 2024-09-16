package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestUtil {
    private TestUtil() {
    }

    public static User getTestUserWithoutMessages() {
        return new User("James", "james@email.com");
    }

    public static User getTestUserWithMessages() {
        User user = new User("Tom", "tom@email.com");
        user.setMessages(new ArrayList<>());
        Message message1 = new Message(null, "test1", user.getName(), LocalDateTime.now());
        Message message2 = new Message(null, "test2", user.getName(), LocalDateTime.now());
        user.getMessages().add(message1);
        user.getMessages().add(message2);
        return user;
    }

    public static Message getNewMessage(User user){
        return new Message(user.getId(), "test1", user.getName(), LocalDateTime.now());
    }
}
