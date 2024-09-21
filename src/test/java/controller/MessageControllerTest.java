package controller;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.dto.UserDto;
import com.aston.infoBoardRestService.mapper.MessageMapper;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.servlet.api.MessageController;
import com.aston.infoBoardRestService.util.LocalDateTimeSerializer;
import com.aston.infoBoardRestService.util.MessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/*
NOT WORKING!
Wanted but not invoked:
messageService.getAllMessages();
-> at controller.MessageControllerTest.testGetAllMessages(MessageControllerTest.java:81)
Actually, there were zero interactions with this mock.

Wanted but not invoked:
messageService.getAllMessages();
-> at controller.MessageControllerTest.testGetAllMessages(MessageControllerTest.java:81)
Actually, there were zero interactions with this mock.
* */

// todo - fix
@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() throws SQLException, JsonProcessingException, ServletException {
        MockitoAnnotations.openMocks(this);
//        messageController.init();
        initMessageControllerMock();
    }

    private void initMessageControllerMock() throws SQLException, JsonProcessingException {
        when(messageService.getAllMessages()).thenReturn(Collections.singletonList(getMessageWithUser()));
//        when(messageService.getMessageById(1L)).thenReturn(getMessageWithUser());
//        when(messageService.getMessagesByAuthorEmail("test@email.com")).thenReturn(Collections.singletonList(getMessageWithUser()));

        String json = "{ \"id\": 1, \"authorId\": 2, \"content\": \"This is a test message.\", \"authorName\": \"author_name1\", \"timestamp\": \"2023-10-01T12:00:00\", \"user\": { \"id\": 1, \"name\": \"author_name1\", \"email\": \"author@example.com\" } }";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        objectMapper.registerModule(javaTimeModule);
        MessageDto messageDto = objectMapper.readValue(json, MessageDto.class);

        doNothing().when(messageService).updateMessage(messageDto);
        doNothing().when(messageService).saveMessage(messageDto);
    }

//    @Test
//    public void testGetAllMessages() throws Exception {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//
//        messageController.doGet(request, response);
//
//        verify(messageService, times(1)).getAllMessages();
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        printWriter.flush();
//        assertEquals("[{\"id\":null,\"authorId\":null,\"content\":null,\"authorName\":null,\"timestamp\":null,\"user\":null}]", stringWriter.toString().trim());
//    }

//    @Test
//    public void testGetMessageById() throws Exception {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        when(request.getPathInfo()).thenReturn("/1");
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//
//        messageController.doGet(request, response);
//
//        verify(messageService, times(1)).getMessageById(1L);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        printWriter.flush();
//        assertEquals("{\"id\":null,\"authorId\":null,\"content\":null,\"authorName\":null,\"timestamp\":null,\"user\":null}", stringWriter.toString().trim());
//    }

//    @Test
//    public void testGetMessagesByAuthorEmail() throws Exception {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        when(request.getPathInfo()).thenReturn("/test@email.com");
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//
//        messageController.doGet(request, response);
//
//        verify(messageService, times(1)).getMessagesByAuthorEmail("test@email.com");
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        printWriter.flush();
//        assertEquals("[{\"id\":null,\"authorId\":null,\"content\":null,\"authorName\":null,\"timestamp\":null,\"user\":null}]", stringWriter.toString().trim());
//    }

    private MessageDto getMessageWithUser() throws JsonProcessingException {
        UserDto userDto = new UserDto();
        MessageDto messageDto = new MessageDto();

        userDto.setId(1000L);
        userDto.setName("James");
        userDto.setEmail("james@email.com");

        messageDto.setId(12345L);
        messageDto.setAuthorId(userDto.getId());
        messageDto.setContent("This is a test message from " + userDto.getName());
        messageDto.setAuthorName(userDto.getName());
        messageDto.setTimestamp(LocalDateTime.now());

        return messageDto;
    }
}
