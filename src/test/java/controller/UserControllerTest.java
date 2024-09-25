package controller;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.mapper.UserMapper;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.servlet.api.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// NOT WORKING
// todo - fix
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

        objectMapper = new ObjectMapper();

        userDtoList = new ArrayList<>();

        message1 = new Message(1L, 1L, "mock message 1", "mockName1", LocalDateTime.now(), null);
        message2 = new Message(2L, 1L, "mock message 2", "mockName1", LocalDateTime.now(), null);
        message3 = new Message(3L, 2L, "mock message 3", "mockName2", LocalDateTime.now(), null);
        message4 = new Message(4L, 2L, "mock message 4", "mockName2", LocalDateTime.now(), null);

        mockUser1 = new User(1L,"mockName1", "mock@email1", List.of(message1, message2));
        mockUser2 = new User(2L, "mockName2", "mock@email2", List.of(message3, message4));

        userDtoList.add(userMapper.toUserDTO(mockUser1));
        userDtoList.add(userMapper.toUserDTO(mockUser2));

        Mockito.when(response.getWriter()).thenReturn(writer);

    }

// todo -> add doGet (3 test)

    @Test
    public void testSaveUserThroughController() throws Exception {

        // Mock request parameters
        Mockito.when(request.getParameter("email")).thenReturn(mockUser1.getEmail());
        Mockito.when(request.getParameter("name")).thenReturn(mockUser1.getName());

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
        assertEquals("User saved successfully", stringWriter.toString().trim());
    }

}
