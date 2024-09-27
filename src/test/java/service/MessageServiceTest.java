package service;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
import com.aston.infoBoardRestService.mapper.MessageMapper;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();
    @Mock
    private MessageDao messageDao;

    private final MessageMapper messageMapper = MessageMapper.INSTANCE;
    private Message message;
    List<Message> messages;

    @BeforeEach
    public void setUp() {
        message = new Message();
        messages = new ArrayList<>();
        User mockUser1 = new User("mockName", "mock@email");

        message.setId(1L);
        message.setContent("mock");
        message.setAuthorName("mockName");
        message.setUser(mockUser1);

        messages.add(message);
    }

    @Test
    @DisplayName("testing getting message by id")
    public void testGetMessageById() throws SQLException {
        Mockito.when(messageDao.getMessage(Mockito.anyLong())).thenReturn(message);
        MessageDto messageById = messageService.getMessageById(1L);
        Assertions.assertEquals("mock", messageById.getContent());
        Mockito.verify(messageDao,Mockito.times(1)).getMessage(Mockito.anyLong());
    }

    @Test
    @DisplayName("SQLException when getting message by ID")
    public void testSQLExceptionWhenGetMessageById() throws SQLException {
        Mockito.when(messageDao.getMessage(Mockito.anyLong())).thenThrow(new SQLException("Database error"));
        Assertions.assertThrows(SQLException.class, () -> {
            messageService.getMessageById(1L);
        });
        Mockito.verify(messageDao, Mockito.times(1)).getMessage(Mockito.anyLong());
    }

    @Test
    @DisplayName("testing getting messages by author ID")
    public void testGetMessagesByAuthorId() throws SQLException {
        Mockito.when(messageDao.getMessagesByAuthorId(Mockito.anyLong())).thenReturn(messages);
        List<MessageDto> messagesByAuthorId = messageService.getMessagesByAuthorId(1L);
        Assertions.assertFalse(messagesByAuthorId.isEmpty());
        Mockito.verify(messageDao, Mockito.times(1)).getMessagesByAuthorId(Mockito.anyLong());
    }

    @Test
    @DisplayName("testing getting messages by author email")
    public void testGetMessagesByAuthorEmail() throws SQLException {
        Mockito.when(messageDao.getMessagesByAuthorEmail(Mockito.anyString())).thenReturn(messages);
        List<MessageDto> messagesByAuthorEmail = messageService.getMessagesByAuthorEmail("mock@email");
        Assertions.assertFalse(messagesByAuthorEmail.isEmpty());
        Mockito.verify(messageDao, Mockito.times(1)).getMessagesByAuthorEmail(Mockito.anyString());
    }

    @Test
    @DisplayName("getting message by non-existent ID")
    public void testGetMessageByNonExistentId() throws SQLException {
        Mockito.when(messageDao.getMessage(Mockito.anyLong())).thenReturn(null);
        MessageDto messageDto = messageService.getMessageById(1L);
        Assertions.assertNull(messageDto);
        Mockito.verify(messageDao, Mockito.times(1)).getMessage(Mockito.anyLong());
    }

    @Test
    @DisplayName("testing getting all the messages")
    public void testGetAllMessages() throws Exception {
        Mockito.when(messageDao.getAllMessages()).thenReturn(messages);
        Assertions.assertFalse(messageService.getAllMessages().isEmpty());
        Mockito.verify(messageDao, Mockito.times(1)).getAllMessages();
    }

    @Test
    @DisplayName("testing saving message")
    public void testSaveMessage() throws Exception {
        Mockito.when(messageDao.saveMessage(message)).thenReturn(true);
        Assertions.assertTrue(messageService.saveMessage(messageMapper.toMessageDto(message)));
        Mockito.verify(messageDao, Mockito.times(1)).saveMessage(message);
    }

    @Test
    @DisplayName("saving message with null fields")
    public void testSaveMessageWithNullFields() throws SQLException {
        MessageDto messageDto = new MessageDto();
        messageDto.setContent(null);
        messageDto.setAuthorName(null);
        Mockito.when(messageDao.saveMessage(messageMapper.toMessage(messageDto))).thenReturn(false);
        Assertions.assertFalse(messageService.saveMessage(messageDto));
        Mockito.verify(messageDao, Mockito.times(1)).saveMessage(messageMapper.toMessage(messageDto));
    }

    @Test
    @DisplayName("SQLException when saving message")
    public void testSQLExceptionWhenSaveMessage() throws SQLException {
        Mockito.when(messageDao.saveMessage(message)).thenThrow(new SQLException("Database error"));
        Assertions.assertThrows(SQLException.class, () -> {
            messageService.saveMessage(messageMapper.toMessageDto(message));
        });
        Mockito.verify(messageDao, Mockito.times(1)).saveMessage(message);
    }

    @Test
    @DisplayName("testing updating message")
    public void testUpdateMessage() throws SQLException {
        Mockito.when(messageDao.updateMessage(message)).thenReturn(true);
        Assertions.assertTrue(messageService.updateMessage(messageMapper.toMessageDto(message)));
        Mockito.verify(messageDao, Mockito.times(1)).updateMessage(message);
    }

    @Test
    @DisplayName("testing updating non-existent message")
    public void testUpdateNonExistentMessage() throws SQLException {
        // Simulate that the message does not exist in the database
        Mockito.when(messageDao.getMessage(Mockito.anyLong())).thenReturn(null);
        Mockito.when(messageDao.updateMessage(Mockito.any(Message.class))).thenCallRealMethod();

        // Create a MessageDto object to update
        MessageDto messageDto = new MessageDto();
        messageDto.setId(999L); // Non-existent ID
        messageDto.setContent("Updated content");
        messageDto.setAuthorName("Updated author");

        // Attempt to update the non-existent message
        boolean result = messageService.updateMessage(messageDto);

        // Verify that the update operation returns false
        Assertions.assertFalse(result);

        // Verify that the getMessage and updateMessage methods were called
        Mockito.verify(messageDao, Mockito.times(1)).getMessage(Mockito.anyLong());
        Mockito.verify(messageDao, Mockito.times(1)).updateMessage(Mockito.any(Message.class));
    }

    @Test
    @DisplayName("testing deleting message")
    public void testDeleteMessage() throws Exception {
        Mockito.when(messageDao.deleteMessage(Mockito.anyLong())).thenReturn(true);
        Assertions.assertTrue(messageService.deleteMessage(1L));
        Mockito.verify(messageDao, Mockito.times(1)).deleteMessage(1L);
    }

}
