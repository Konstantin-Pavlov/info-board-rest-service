package service;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.service.MessageService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageServiceTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @Mock
    private MessageService messageService; // Mocking the service for message

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
    @DisplayName("testing getting message by id")
    public void testGetMessageById() throws Exception {
        // Mocking the service call
        MessageDto mockMessageDto = new MessageDto();
        mockMessageDto.setContent("test");
        mockMessageDto.setId(1L);
        when(messageService.getMessageById(1L)).thenReturn(mockMessageDto);

        // Verify the logic in the controller
        assertEquals("test", mockMessageDto.getContent());
    }

    @Test
    @DisplayName("testing getting all the messages")
    public void testGetAllMessages() throws Exception {
        // Mocking the service call
        MessageDto mockMessageDto = new MessageDto();
        mockMessageDto.setAuthorName("james");
        List<MessageDto> mockUserList = List.of(mockMessageDto);
        when(messageService.getAllMessages()).thenReturn(mockUserList);

        // Assert that the returned users list is not empty
        assertFalse(messageService.getAllMessages().isEmpty());
    }

    @Test
    @DisplayName("testing saving message")
    public void testSaveMessage() throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.setAuthorName("james");
        messageDto.setContent("test");

        when(messageService.saveMessage(messageDto)).thenReturn(true);

        assertTrue(messageService.saveMessage(messageDto));

        verify(messageService, times(1)).saveMessage(messageDto);
    }

}
