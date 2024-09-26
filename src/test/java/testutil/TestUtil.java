package testutil;

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

        user.getMessages().add(getNewMessage(user, "test1"));
        user.getMessages().add(getNewMessage(user, "test2"));

        return user;
    }

    public static Message getNewMessage(User user, String content){
        return new Message(user.getId(), content, user.getName(), LocalDateTime.now(), user);
    }
}
