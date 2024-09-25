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
    @DisplayName("testing deleting message")
    public void testDeleteMessage() throws Exception {
        Mockito.when(messageDao.deleteMessage(Mockito.anyLong())).thenReturn(true);
        Assertions.assertTrue(messageService.deleteMessage(1L));
        Mockito.verify(messageDao, Mockito.times(1)).deleteMessage(1L);
    }

}
