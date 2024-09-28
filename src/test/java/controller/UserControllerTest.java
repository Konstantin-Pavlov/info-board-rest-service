package controller;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.mapper.UserMapper;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.servlet.api.UserController;
import com.aston.infoBoardRestService.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

public class UserControllerTest {
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PrintWriter writer;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User mockUser1;
    private User mockUser2;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    List<UserDto> userDtoList;
    List<MessageDto> messageDtos;

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        userController.init();

        objectMapper = new ObjectMapper().registerModule(
                new JavaTimeModule()
                        .addSerializer(
                                LocalDateTime.class, new LocalDateTimeSerializer()
                        )
        );

        userDtoList = new ArrayList<>();

        message1 = new Message(1L, 1L, "mock message 1", "mockName1", LocalDateTime.now(), null);
        message2 = new Message(2L, 1L, "mock message 2", "mockName1", LocalDateTime.now(), null);
        message3 = new Message(3L, 2L, "mock message 3", "mockName2", LocalDateTime.now(), null);
        message4 = new Message(4L, 2L, "mock message 4", "mockName2", LocalDateTime.now(), null);

        mockUser1 = new User(1L, "mockName1", "mock@email1", List.of(message1, message2));
        mockUser2 = new User(2L, "mockName2", "mock@email2", List.of(message3, message4));

        userDtoList.add(userMapper.toUserDTO(mockUser1));
        userDtoList.add(userMapper.toUserDTO(mockUser2));

        Mockito.when(response.getWriter()).thenReturn(writer);

    }

    @DisplayName("test: get user by email")
    @Test
    public void testGetUserByEmail() throws Exception {
        Mockito.when(userService.getUserByEmail("mock@email1")).thenReturn(userMapper.toUserDTO(mockUser1));
        Mockito.when(request.getPathInfo()).thenReturn("/mock@email1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        userController.doGet(request, response);

        Mockito.verify(userService, Mockito.times(1)).getUserByEmail("mock@email1");
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString().trim();
        assertEquals(objectMapper.writeValueAsString(userMapper.toUserDTO(mockUser1)), jsonResponse);
        assertEquals(objectMapper.writeValueAsString(userMapper.toUserDTO(mockUser1)), stringWriter.toString().trim());
    }

    @DisplayName("test: get user by email - not found")
    @Test
    public void testGetUserByEmailNotFound() throws Exception {
        Mockito.when(request.getPathInfo()).thenReturn("/nonexistent@example.com");

        // Mock the service to return null when the user is not found
        Mockito.when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        // Verify the service call and response status
        Mockito.verify(userService, Mockito.times(1)).getUserByEmail("nonexistent@example.com");
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Validate the error message
        writer.flush();
        assertEquals("User with email nonexistent@example.com not found", stringWriter.toString().trim());
    }

    @DisplayName("test: get user by id")
    @Test
    public void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(2L)).thenReturn(userMapper.toUserDTO(mockUser1));
        Mockito.when(request.getPathInfo()).thenReturn("/2");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        userController.doGet(request, response);

        Mockito.verify(userService, Mockito.times(1)).getUserById(2L);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString().trim();
        assertEquals(objectMapper.writeValueAsString(userMapper.toUserDTO(mockUser1)), jsonResponse);
        assertEquals(objectMapper.writeValueAsString(userMapper.toUserDTO(mockUser1)), stringWriter.toString().trim());
    }

    @DisplayName("test: get user by id - invalid id format")
    @Test
    public void testGetUserByIdInvalidFormat() throws Exception {
        // Invalid ID in the path
        Mockito.when(request.getPathInfo()).thenReturn("/2g3df2");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        // Verify no service call and correct response status
        Mockito.verify(userService, Mockito.times(0)).getUserById(Mockito.anyLong());
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Verify the error message
        writer.flush();
        assertEquals("{\"message\": \"Invalid ID format: 2g3df2\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: user not found exception handling")
    @Test
    public void testUserNotFoundExceptionHandling() throws Exception {
        Mockito.when(request.getPathInfo()).thenReturn("/1");

        // Mock the service to throw UserNotFoundException
        Mockito.when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("User not found"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        // Verify the service call
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Check the error message
        writer.flush();
        assertEquals("{\"message\": \"Error retrieving user: User not found\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: get all users")
    @Test
    public void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(userDtoList);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString();
        Assertions.assertTrue(jsonResponse.contains("mock@email1"));
        Assertions.assertTrue(jsonResponse.contains("mock@email2"));
        assertEquals(objectMapper.writeValueAsString(userDtoList), stringWriter.toString().trim());
    }

    @DisplayName("test: get all users - empty list")
    @Test
    public void testGetAllUsersEmptyList() throws Exception {
        // Mock an empty list of users
        Mockito.when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        // Verify the service and status
        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);

        // No content should be written
        assertEquals("", stringWriter.toString().trim());
    }

    @DisplayName("test: get all users - sql exception")
    @Test
    public void testGetAllUsersSQLException() throws Exception {
        Mockito.when(request.getPathInfo()).thenReturn(null);

        // Mock the service to throw a SQLException
        Mockito.when(userService.getAllUsers()).thenThrow(new SQLException("Database error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        userController.doGet(request, response);

        // Verify the service call and the response status
        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Validate the error message
        writer.flush();
        assertEquals("{\"message\": \"Error retrieving users: Database error\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: save user")
    @Test
    public void testSaveUser() throws Exception {
        // Create a UserDto object to be sent as JSON
        UserDto userDto = new UserDto();
        userDto.setEmail(mockUser1.getEmail());
        userDto.setName(mockUser1.getName());

        // Convert the UserDto object to JSON
        String jsonUser = objectMapper.writeValueAsString(userDto);

        // Mock the request reader to return the JSON data
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonUser)));

        // Mock the service call
        Mockito.when(userService.saveUser(any(UserDto.class))).thenReturn(true);

        // Prepare the response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        // Call the doPost method
        userController.doPost(request, response);

        // Verify the service call
        ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        Mockito.verify(userService, Mockito.times(1)).saveUser(userDtoCaptor.capture());
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        // Verify the captured UserDto
        UserDto capturedUser = userDtoCaptor.getValue();
        assertEquals(mockUser1.getEmail(), capturedUser.getEmail());
        assertEquals(mockUser1.getName(), capturedUser.getName());

        // Ensure all data is written to the stringWriter
        writer.flush();
        assertEquals("{\"message\": \"User saved successfully\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: save user fails")
    @Test
    public void testSaveUserFails() throws Exception {
        // Create a UserDto object to be sent as JSON
        UserDto userDto = new UserDto();
        userDto.setEmail(mockUser1.getEmail());
        userDto.setName(mockUser1.getName());

        // Convert the UserDto object to JSON
        String jsonUser = objectMapper.writeValueAsString(userDto);

        // Mock the request reader to return the JSON data
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonUser)));

        // Mock the service call to return false (simulate saving failure)
        Mockito.when(userService.saveUser(any(UserDto.class))).thenReturn(false);

        // Prepare the response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        // Call the doPost method
        userController.doPost(request, response);

        // Verify the service call
        Mockito.verify(userService, Mockito.times(1)).saveUser(any(UserDto.class));
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Ensure all data is written to the stringWriter
        writer.flush();
        assertEquals("{\"message\": \"Failed to save user\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: save user with invalid input")
    @Test
    public void testSaveUserInvalidInput() throws Exception {
        // Create a UserDto object with missing email and name
        UserDto userDto = new UserDto();
        userDto.setEmail(null);
        userDto.setName(null);

        // Convert the UserDto object to JSON
        String jsonUser = objectMapper.writeValueAsString(userDto);

        // Mock the request reader to return the JSON data
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonUser)));

        // Prepare the response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        // Call the doPost method
        userController.doPost(request, response);

        // Verify no service call is made
        Mockito.verify(userService, Mockito.never()).saveUser(any(UserDto.class));

        // Verify the response status and message
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertEquals("{\"message\": \"Invalid input: email and name are required\"}", stringWriter.toString().trim());
    }

    @DisplayName("test: exception during user saving")
    @Test
    public void testSaveUserThrowsException() throws Exception {
        // Create a UserDto object to be sent as JSON
        UserDto userDto = new UserDto();
        userDto.setEmail(mockUser1.getEmail());
        userDto.setName(mockUser1.getName());

        // Convert the UserDto object to JSON
        String jsonUser = objectMapper.writeValueAsString(userDto);

        // Mock the request reader to return the JSON data
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonUser)));

        // Mock the service call to throw a SQLException
        Mockito.when(userService.saveUser(any(UserDto.class))).thenThrow(new SQLException("Database error"));

        // Prepare the response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        // Call the doPost method
        userController.doPost(request, response);

        // Verify the service call and the response status
        Mockito.verify(userService, Mockito.times(1)).saveUser(any(UserDto.class));
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Ensure all data is written to the stringWriter
        writer.flush();
        assertEquals("{\"message\": \"Error saving user: Database error\"}", stringWriter.toString().trim());
    }

}
