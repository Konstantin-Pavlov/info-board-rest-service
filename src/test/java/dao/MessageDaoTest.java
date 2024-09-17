package dao;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageDaoTest {
    private static final UserDao userDao = new UserDao();
    private static final MessageDao messageDao = new MessageDao();
    private static User userWithMessages;
    private static User dbUser;
    private static Message message;

    @BeforeAll
    public static void setUp() throws SQLException {
        userWithMessages = TestUtil.getTestUserWithMessages();
        userDao.saveUser(userWithMessages);
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        dbUser = userDao
                .getUserByEmail(userWithMessages.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("Can't find user with email: " + userWithMessages.getEmail())
                );
        dbUser.setMessages(new ArrayList<>(2));
        dbUser.getMessages().add(TestUtil.getNewMessage(dbUser));
        dbUser.getMessages().add(TestUtil.getNewMessage(dbUser));
    }

    @Test
    @DisplayName("save message")
    public void testSaveMessage() throws SQLException {
        Message message = TestUtil.getNewMessage(dbUser);
        Assertions.assertTrue(messageDao.saveMessage(message));
    }

    @Test
    @DisplayName("get message")
    public void testGetMessage() throws SQLException {
        Assertions.assertFalse(messageDao.getMessagesByAuthorEmail(dbUser.getEmail()).isEmpty());
    }

    @Test
    @DisplayName("delete message and make sure it's deleted")
    public void testDeleteMessage() throws SQLException {
        Message message = messageDao.getMessagesByAuthorEmail(dbUser.getEmail()).get(0);
        Assertions.assertTrue(messageDao.deleteMessage(message.getId()));

        Assertions.assertNull(messageDao.getMessage(message.getId()));
    }

}
