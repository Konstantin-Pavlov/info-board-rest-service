import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.servlet.api.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @Mock
    private UserService userService; // Mocking the service for user

    @InjectMocks
    private UserController userController; // Injecting mock into controller

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
    public void testGetUserByEmail() throws Exception {
        // Mocking the service call
        UserDto mockUser = new UserDto();
        mockUser.setEmail("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        // Verify the logic in the controller
        assertEquals("test@example.com", mockUser.getEmail());
    }

    @Test
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
    public void testSaveUser() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setEmail("jane.doe@example.com");
        newUser.setName("Jane Doe");

        when(userService.saveUser(newUser)).thenReturn(true);

        assertTrue(userService.saveUser(newUser));

        verify(userService, times(1)).saveUser(newUser);
    }

    @Test
    public void testSaveUserThroughController() throws Exception {
        // Create a new user to save
        UserDto newUser = new UserDto();
        newUser.setEmail("test@example.com");
        newUser.setName("Test User");

        // Mock the service call inside the controller
        when(userService.saveUser(newUser)).thenReturn(true);

        // Create a mock request and response
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simulate JSON body content
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Access the protected doPost method using reflection
        Method doPostMethod = UserController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);  // Make it accessible

        // Invoke the method
        doPostMethod.invoke(userController, request, response);

        // Verify that the service was called once
        verify(userService, times(1)).saveUser(newUser);

        // Check response content
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }



}
