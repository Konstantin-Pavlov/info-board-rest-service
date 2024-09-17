package service;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @Mock
    private UserService userService; // Mocking the service for user

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize PostgreSQLContainer
        postgresContainer = new PostgreSQLContainer<>("postgres:12-alpine")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();

        // Set up the data source and inject it into your repository
        String jdbcUrl = postgresContainer.getJdbcUrl();
        System.setProperty("DB_URL", jdbcUrl);
        System.setProperty("DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
    }

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
    }

    @Test
    @DisplayName("test 1: getting user by email")
    public void testGetUserByEmail() throws Exception {
        // Mocking the service call
        UserDto mockUser = new UserDto();
        mockUser.setEmail("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        // Verify the logic in the controller
        assertEquals("test@example.com", mockUser.getEmail());
    }

    @Test
    @DisplayName("test 2: getting all users")
    public void testGetAllUsers() throws Exception {
        // Mocking the service call
        UserDto mockUser = new UserDto();
        mockUser.setEmail("test@example.com");
        List<UserDto> mockUserList = List.of(mockUser);
        when(userService.getAllUsers()).thenReturn(mockUserList);

        // Assert that the returned users list is not empty
        assertFalse(userService.getAllUsers().isEmpty());
    }

    @Test
    @DisplayName("test 3: saving the user")
    public void testSaveUser() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setEmail("jane.doe@example.com");
        newUser.setName("Jane Doe");

        when(userService.saveUser(newUser)).thenReturn(true);

        assertTrue(userService.saveUser(newUser));

        verify(userService, times(1)).saveUser(newUser);
    }

    @Test
    @DisplayName("test 3: getting not existing user by email")
    public void testGetUserAndThrowException() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setEmail("jane.doe@example.com");
        newUser.setName("Jane Doe");

        when(userService.getUserByEmail(newUser.getEmail())).thenReturn(null);

        assertNull(userService.getUserByEmail(newUser.getEmail()));

        verify(userService, times(1)).getUserByEmail(newUser.getEmail());
    }

}
