package dao;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class UserDaoTest {
    private static final UserDao userDao = new UserDao();
    private final MessageDao messageDao = new MessageDao();
    private static User userWithoutMessages;
    private static User userWithMessages;

    @BeforeAll
    public static void setUp() throws SQLException {
        userWithoutMessages = TestUtil.getTestUserWithoutMessages();
        userWithMessages = TestUtil.getTestUserWithMessages();

        if (userDao.getUserByEmail(userWithoutMessages.getEmail()).isPresent()) {
            userDao.deleteUser(userWithoutMessages.getEmail());
        }

        if (userDao.getUserByEmail(userWithMessages.getEmail()).isPresent()) {
            userDao.deleteUser(userWithMessages.getEmail());
        }
    }

    @Test
    @DisplayName("save user and try to save them again")
    public void saveUserWithoutMessages() throws SQLException {
        Assertions.assertTrue(userDao.saveUser(userWithoutMessages));
        Assertions.assertFalse(userDao.saveUser(userWithoutMessages));
    }

    @Test
    @DisplayName("test that user's message list is empty")
    public void getUserMessagesFromUserWithoutMessages() throws SQLException {
        List<Message> messagesByAuthorEmail = messageDao.getMessagesByAuthorEmail(userWithoutMessages.getEmail());
        Assertions.assertTrue(messagesByAuthorEmail.isEmpty());
    }

    @Test
    @DisplayName("test saving user with messages list")
    public void saveUserWithMessages() throws SQLException {
        Assertions.assertTrue(userDao.saveUser(userWithMessages));
    }

    @Test
    @DisplayName("test user with messages")
    public void getUserMessagesFromUserWithMessages() throws SQLException {
        List<Message> messagesByAuthorEmail = messageDao.getMessagesByAuthorEmail(userWithMessages.getEmail());
        Assertions.assertEquals(2, messagesByAuthorEmail.size());
    }

//    @Test
//    @DisplayName("check if user and their messages has been deleted")
//    public void delCheck() throws SQLException {
//        User dbUser = userDao.getUserByEmail(userWithMessages.getEmail()).orElseThrow(
//                () -> new UserNotFoundException("Can't find user with email: " + userWithMessages.getEmail())); // get id's
//
//        Assertions.assertTrue(userDao.deleteUser(userWithMessages.getEmail()));
//
//        Assertions.assertThrows(UserNotFoundException.class, ()-> userDao.getUserByEmail(userWithMessages.getEmail()));
//
//        List<Message> messagesByAuthorId = messageDao.getMessagesByAuthorId(dbUser.getId());
//        Assertions.assertTrue(messagesByAuthorId.isEmpty());
//    }

}
