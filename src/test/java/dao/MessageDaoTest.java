package dao;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.util.DbUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Testcontainers
public class MessageDaoTest {
    private final Logger logger = Logger.getLogger(OrderDaoTest.class.getName());

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private MessageDao messageDao;

    @BeforeAll
    public static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up the data source and inject it into your repository
        DbUtil.setConnectionDetails(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        messageDao = new MessageDao();

        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());
             Statement stmt = connection.createStatement()) {
            // Create users table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            username VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE
                        )
                    """);

            // Create messages table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS messages (
                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            author_id BIGINT NOT NULL,
                            content VARCHAR(255) NOT NULL,
                            author_name VARCHAR(255) NOT NULL,
                            timestamp TIMESTAMP NOT NULL,
                            CONSTRAINT fk_message_author FOREIGN KEY (author_id) REFERENCES users(id)
                        )
                    """);

            // Insert test data into users table
            stmt.execute("INSERT INTO users (username, email) VALUES ('John Doe', 'john.doe@example.com')");
            stmt.execute("INSERT INTO users (username, email) VALUES ('Jane Smith', 'jane.smith@example.com')");

            // Insert test data into messages table
            stmt.execute("INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (1, 'Hello, world!', 'John Doe', '2023-10-01T10:00:00')");
            stmt.execute("INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (1, 'Hello, this is message from John', 'John Doe', '2023-10-01T10:57:17')");
            stmt.execute("INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (2, 'Hi there!', 'Jane Smith', '2023-10-01T11:00:00')");
            stmt.execute("INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (2, 'Hi Hello, this is message from Jane', 'Jane Smith', '2023-10-01T11:01:09')");

        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test, if needed
        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE messages, users RESTART IDENTITY CASCADE");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }


    @Test
    @DisplayName("test: get all messages")
    public void testGetAllMessages() throws SQLException {
        List<Message> messages = messageDao.getAllMessages();
        Assertions.assertNotNull(messages);
        Assertions.assertEquals(4, messages.size());
    }

    @Test
    @DisplayName("test: get messages by author email (no result)")
    public void testGetMessagesByAuthorEmail_NoResult() throws SQLException {
        List<Message> messages = messageDao.getMessagesByAuthorEmail("unknown@example.com");

        Assertions.assertNotNull(messages);
        Assertions.assertTrue(messages.isEmpty());
    }

    @Test
    @DisplayName("test: get message by id")
    public void testGetMessageById() throws SQLException {
        Message message = messageDao.getMessage(1L);

        Assertions.assertNotNull(message);
        Assertions.assertEquals(1L, message.getId());
        Assertions.assertEquals(1L, message.getAuthorId());
        Assertions.assertEquals("Hello, world!", message.getContent());
        Assertions.assertEquals("John Doe", message.getAuthorName());
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0), message.getTimestamp());
        Assertions.assertNotNull(message.getUser());
        Assertions.assertEquals("John Doe", message.getUser().getName());
    }

    @Test
    @DisplayName("test: save message and then get it")
    public void testSaveMessage() throws SQLException {
        Message message = new Message(2L, "it's Jane, good morning!", "Jane Smith", LocalDateTime.of(2023, 10, 19, 9, 2, 42));
        Assertions.assertTrue(messageDao.saveMessage(message));

        Assertions.assertEquals(3, messageDao.getMessagesByAuthorEmail("jane.smith@example.com").size());

        Message savedMessage = messageDao.getMessage(5L);

        Assertions.assertNotNull(savedMessage);
        Assertions.assertEquals(2L, savedMessage.getAuthorId());
        Assertions.assertEquals("it's Jane, good morning!", savedMessage.getContent());
        Assertions.assertEquals("Jane Smith", savedMessage.getUser().getName());
    }

    @Test
    @DisplayName("test: save message with invalid data")
    public void testSaveInvalidMessage() {
        Message invalidMessage = new Message(null, null, "Invalid Author", LocalDateTime.now());

        Assertions.assertThrows(NullPointerException.class, () -> messageDao.saveMessage(invalidMessage));
    }

    @Test
    @DisplayName("test: save message with non-existent author_id")
    public void testSaveMessageWithNonExistentAuthorId() throws SQLException {
        Message message = new Message(999L, "This author does not exist", "Unknown Author", LocalDateTime.now());

        boolean result = messageDao.saveMessage(message);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("test: update a message")
    public void testUpdateMessage() throws SQLException {
        Message message = messageDao.getMessage(1L);
        message.setContent("Updated content");

        Assertions.assertTrue(messageDao.updateMessage(message));

        Message updatedMessage = messageDao.getMessage(1L);
        Assertions.assertEquals("Updated content", updatedMessage.getContent());
    }

    @Test
    @DisplayName("test: update not existing message")
    public void testUpdateNotExistingMessage() throws SQLException {
        Message message = new Message(23565L, 24567L, "", "Jane Smith", LocalDateTime.of(2023, 10, 19, 9, 2, 42), null);
        message.setContent("Updated content");
        Assertions.assertFalse(messageDao.updateMessage(message));
    }


    @Test
    @DisplayName("test: delete message and make sure it's deleted")
    public void testDeleteMessage() throws SQLException {
        Message message = new Message(2L, "it's Jane, good morning!", "Jane Smith", LocalDateTime.of(2023, 10, 19, 9, 2, 42));
        Assertions.assertTrue(messageDao.saveMessage(message));

        List<Message> messagesByAuthorEmail = messageDao.getMessagesByAuthorEmail("jane.smith@example.com");
        Assertions.assertEquals(3, messagesByAuthorEmail.size());

        Message messageToDelete = messagesByAuthorEmail.get(2);

        Assertions.assertTrue(messageDao.deleteMessage(messageToDelete.getId()));
        Assertions.assertEquals(2, messageDao.getMessagesByAuthorEmail("jane.smith@example.com").size());
    }

    @Test
    @DisplayName("test: delete non-existing message")
    public void testDeleteNonExistingMessage() throws SQLException {
        boolean result = messageDao.deleteMessage(999L);
        Assertions.assertFalse(result);
    }

}
