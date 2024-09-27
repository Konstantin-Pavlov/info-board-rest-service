package service;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@MockitoSettings(strictness = Strictness.LENIENT)
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
    @DisplayName("test 4: deleting the user")
    public void testDeleteUser() throws SQLException {
        Mockito.when(userDao.deleteUser("mock@email")).thenReturn(true);
        Assertions.assertTrue(userService.deleteUser("mock@email"));
        Mockito.verify(userDao, times(1)).deleteUser("mock@email");
    }

    @Test
    @DisplayName("test 5: getting user by ID")
    public void testGetUserById() throws SQLException {
        Mockito.when(userDao.getUserById(1L)).thenReturn(Optional.of(mockUser));
        UserDto userDto = userService.getUserById(1L);
        User actualUser = userMapper.ToUser(userDto);
        Assertions.assertEquals(mockUser, actualUser);
        Mockito.verify(userDao, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("test 6: getting user by non-existent ID")
    public void testGetUserByNonExistentId() throws SQLException {
        Mockito.when(userDao.getUserById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(2L);
        });
        Mockito.verify(userDao, times(1)).getUserById(2L);
    }

    @Test
    @DisplayName("test 7: SQLException when getting user by email")
    public void testSQLExceptionWhenGetUserByEmail() throws SQLException {
        Mockito.when(userDao.getUserByEmail("mock@email")).thenThrow(new SQLException("Database error"));
        Assertions.assertThrows(SQLException.class, () -> {
            userService.getUserByEmail("mock@email");
        });
        Mockito.verify(userDao, times(1)).getUserByEmail("mock@email");
    }

    @Test
    @DisplayName("test 8: SQLException when saving user")
    public void testSQLExceptionWhenSaveUser() throws SQLException {
        Mockito.when(userDao.saveUser(mockUser)).thenThrow(new SQLException("Database error"));
        Assertions.assertThrows(SQLException.class, () -> {
            userService.saveUser(userMapper.toUserDTO(mockUser));
        });
        Mockito.verify(userDao, times(1)).saveUser(mockUser);
    }

    @Test
    @DisplayName("test 9: saving user with null fields")
    public void testSaveUserWithNullFields() throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setName(null);
        userDto.setEmail(null);
        Mockito.when(userDao.saveUser(userMapper.ToUser(userDto))).thenReturn(false);
        Assertions.assertFalse(userService.saveUser(userDto));
        Mockito.verify(userDao, times(1)).saveUser(userMapper.ToUser(userDto));
    }

    @Test
    @DisplayName("test 10: getting user by non-existent email")
    public void testGetUserByNonExistentEmail() throws SQLException {
        Mockito.when(userDao.getUserByEmail("nonexistent@email")).thenReturn(null);
        UserDto userDto = userService.getUserByEmail("nonexistent@email");
        Assertions.assertNull(userDto);
        Mockito.verify(userDao, times(1)).getUserByEmail("nonexistent@email");
    }

}
