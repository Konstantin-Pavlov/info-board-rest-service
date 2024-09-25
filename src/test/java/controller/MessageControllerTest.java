package controller;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.mapper.MessageMapper;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.servlet.api.MessageController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    @Mock
    private MessageService messageService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PrintWriter writer;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private MessageController messageController;
    private ObjectMapper objectMapper;
    private Message message1;
    private Message message2;
    List<MessageDto> messageDtos;

    @BeforeEach
    void setUp() throws IOException, ServletException {
        MockitoAnnotations.openMocks(this);
        messageController.init();

        objectMapper = new ObjectMapper();

        message1 = new Message();
        message2 = new Message();
        messageDtos = new ArrayList<>();
        User mockUser = new User("mockName", "mock@email");

        message1.setId(1L);
        message1.setContent("mock1");
        message1.setAuthorName("mockName");
        message1.setUser(mockUser);

        message2.setId(2L);
        message2.setContent("mock2");
        message2.setAuthorName("mockName");
        message2.setUser(mockUser);

        messageDtos.add(messageMapper.toMessageDto(message1));
        messageDtos.add(messageMapper.toMessageDto(message2));

        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testGetAllMessages() throws Exception {
        Mockito.when(messageService.getAllMessages()).thenReturn(messageDtos);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        messageController.doGet(request, response);

        Mockito.verify(messageService, Mockito.times(1)).getAllMessages();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString();
        Assertions.assertTrue(jsonResponse.contains("mock1"));
        Assertions.assertTrue(jsonResponse.contains("mock2"));
        assertEquals(objectMapper.writeValueAsString(messageDtos), stringWriter.toString().trim());
    }

    @Test
    public void testGetMessageById() throws IOException, SQLException {

        Mockito.when(messageService.getMessageById(1L)).thenReturn(messageMapper.toMessageDto(message1));
        Mockito.when(request.getPathInfo()).thenReturn("/1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        messageController.doGet(request, response);

        verify(messageService, Mockito.times(1)).getMessageById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        printWriter.flush();
        assertEquals(objectMapper.writeValueAsString(messageMapper.toMessageDto(message1)), stringWriter.toString().trim());
    }

    @Test
    public void testGetMessagesByAuthorEmail() throws Exception {
        Mockito.when(messageService.getMessagesByAuthorEmail("mock@email")).thenReturn(messageDtos);
        Mockito.when(request.getPathInfo()).thenReturn("/mock@email");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        messageController.doGet(request, response);

        verify(messageService, Mockito.times(1)).getMessagesByAuthorEmail("mock@email");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString();
        Assertions.assertTrue(jsonResponse.contains("mock1"));
        Assertions.assertTrue(jsonResponse.contains("mock2"));
        assertEquals(objectMapper.writeValueAsString(messageDtos), stringWriter.toString().trim());
    }

}