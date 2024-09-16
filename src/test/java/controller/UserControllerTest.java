package controller;

import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.servlet.api.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// NOT WORKING
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


    // todo - fix
    // use spy?
    @Test
    public void testSaveUserThroughController() throws Exception {
        // Create a new user to save
        UserDto newUser = new UserDto();
        newUser.setEmail("test@example.com");
        newUser.setName("Test User");

        // Mock the service call inside the controller
        when(userService.saveUser(any(UserDto.class))).thenReturn(true);

        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);


        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Access the protected doPost method using reflection
        Method doPostMethod = UserController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Invoke the method
        doPostMethod.invoke(userController, request, response);

        // Verify that the service was called
        ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, times(1)).saveUser(userDtoCaptor.capture());

        // Verify the captured UserDto
        UserDto capturedUser = userDtoCaptor.getValue();
        assertEquals(newUser.getEmail(), capturedUser.getEmail());
        assertEquals(newUser.getName(), capturedUser.getName());

        // Verify response status and content
        verify(response).setStatus(HttpServletResponse.SC_OK);
        printWriter.flush(); // Ensure all data is written to the stringWriter
        assertEquals("User saved successfully", stringWriter.toString().trim());
    }

}
