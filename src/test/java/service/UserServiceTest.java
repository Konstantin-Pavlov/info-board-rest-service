package service;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.mapper.UserMapper;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private User mockUser;
    private Message message;
    List<Message> messages;
    List<User> users;

    @BeforeEach
    void setUp() {
        messages = List.of(
                new Message(1L, "mock1", "mockName", LocalDateTime.now()),
                new Message(1L, "mock2", "mockName", LocalDateTime.now())
        );

        mockUser = new User(1L, "mockName", "mock@email", messages);

        users = List.of(mockUser);
    }

    @Test
    @DisplayName("test 1: getting user by email")
    public void testGetUserByEmail() throws SQLException {
        Mockito.when(userDao.getUserByEmail("mock@email")).thenReturn(mockUser);
        UserDto userDto = userService.getUserByEmail("mock@email");
        Assertions.assertEquals(mockUser, userMapper.ToUser(userDto));
        Mockito.verify(userDao, times(1)).getUserByEmail("mock@email");
    }

    @Test
    @DisplayName("test 2: getting all users")
    public void testGetAllUsers() throws SQLException {
        Mockito.when(userDao.getAllUsers()).thenReturn(users);
        Assertions.assertFalse(userService.getAllUsers().isEmpty());
        Mockito.verify(userDao, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("test 3: saving the user")
    public void testSaveUser() throws SQLException {
        Mockito.when(userDao.saveUser(mockUser)).thenReturn(true);
        Assertions.assertTrue(userService.saveUser(userMapper.toUserDTO(mockUser)));
        Mockito.verify(userDao, times(1)).saveUser(mockUser);
    }

    @Test
    @DisplayName("test 3: deleting the user")
    public void testDeleteUser() throws SQLException {
        Mockito.when(userDao.deleteUser("mock@email")).thenReturn(true);
        Assertions.assertTrue(userService.deleteUser("mock@email"));
        Mockito.verify(userDao, times(1)).deleteUser("mock@email");
    }

}
