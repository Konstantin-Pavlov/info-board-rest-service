package dao;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.Message;
import com.aston.infoBoardRestService.entity.User;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDaoTest {
    private final Logger logger = Logger.getLogger(OrderDaoTest.class.getName());

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private UserDao userDao;
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

        userDao = new UserDao();
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
            stmt.execute("INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (2, 'what''s up, it''s Jane', 'Jane Smith', '2023-10-14T22:54:41')");
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
    @DisplayName("test: get all users")
    public void testGetAllUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(2, users.get(0).getMessages().size());
        Assertions.assertEquals(3, users.get(1).getMessages().size());
    }

    @Test
    @DisplayName("test: get user by id")
    public void testGetUserById() throws SQLException {
        Optional<User> userOptional = userDao.getUserById(2L);

        // Check that the user is present
        Assertions.assertTrue(userOptional.isPresent(), "User should be present");
        User user = userOptional.get();

        // Check that the user is not null
        Assertions.assertNotNull(user, "User should not be null");

        // Check user properties
        Assertions.assertEquals(2L, user.getId(), "User ID should be 2");
        Assertions.assertEquals("Jane Smith", user.getName(), "User name should be 'Jane Smith'");
        Assertions.assertEquals("jane.smith@example.com", user.getEmail(), "User email should be 'jane.smith@example.com'");

        // Check the number of messages
        List<Message> messages = user.getMessages();
        Assertions.assertNotNull(messages, "Messages list should not be null");
        Assertions.assertEquals(3, messages.size(), "User should have 3 messages");

        // Check properties of each message
        Message message1 = messages.get(0);
        Assertions.assertEquals(2L, message1.getAuthorId(), "Message 1 author ID should be 2");
        Assertions.assertEquals("Hi there!", message1.getContent(), "Message 1 content should be 'Hi there!'");
        Assertions.assertEquals("Jane Smith", message1.getAuthorName(), "Message 1 author name should be 'Jane Smith'");
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0), message1.getTimestamp(), "Message 1 timestamp should be '2023-10-01T11:00:00'");

        Message message2 = messages.get(1);
        Assertions.assertEquals(2L, message2.getAuthorId(), "Message 2 author ID should be 2");
        Assertions.assertEquals("Hi Hello, this is message from Jane", message2.getContent(), "Message 2 content should be 'Hi Hello, this is message from Jane'");
        Assertions.assertEquals("Jane Smith", message2.getAuthorName(), "Message 2 author name should be 'Jane Smith'");
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 1, 11, 1, 9), message2.getTimestamp(), "Message 2 timestamp should be '2023-10-01T11:01:09'");

        Message message3 = messages.get(2);
        Assertions.assertEquals(2L, message3.getAuthorId(), "Message 3 author ID should be 2");
        Assertions.assertEquals("what's up, it's Jane", message3.getContent(), "Message 3 content should be 'what's up, it's Jane'");
        Assertions.assertEquals("Jane Smith", message3.getAuthorName(), "Message 3 author name should be 'Jane Smith'");
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 14, 22, 54, 41), message3.getTimestamp(), "Message 3 timestamp should be '2023-10-01T22:54:41'");
    }

    @Test
    @DisplayName("test: get user by email")
    public void testGetUserByEmail() throws SQLException {
        User user = userDao.getUserByEmail("john.doe@example.com");

        // Check that the user is not null
        Assertions.assertNotNull(user, "User should not be null");

        // Check user properties
        Assertions.assertEquals(1L, user.getId(), "User ID should be 1");
        Assertions.assertEquals("John Doe", user.getName(), "User name should be 'John Doe'");
        Assertions.assertEquals("john.doe@example.com", user.getEmail(), "User email should be 'john.doe@example.com'");

        // Check the number of messages
        List<Message> messages = user.getMessages();
        Assertions.assertNotNull(messages, "Messages list should not be null");
        Assertions.assertEquals(2, messages.size(), "User should have 2 messages");
    }

    @Test
    @DisplayName("test: save user with messages")
    public void testSaveUserWithMessages() throws SQLException {
        // Step 1: Create a new User object with associated Message objects
        User newUser = new User("Alice Johnson", "alice.johnson@example.com");
        Message message1 = new Message(null, "Hello, this is Alice!", "Alice Johnson", LocalDateTime.of(2023, 10, 20, 10, 0));
        Message message2 = new Message(null, "Another message from Alice", "Alice Johnson", LocalDateTime.of(2023, 10, 20, 11, 0));
        newUser.setMessages(List.of(message1, message2));

        // Step 2: Save the User object using the UserDao
        boolean isSaved = userDao.saveUser(newUser);
        Assertions.assertTrue(isSaved, "User should be saved successfully");

        // Step 3: Retrieve the saved User object from the database
        User savedUser = userDao.getUserByEmail("alice.johnson@example.com");
        Assertions.assertNotNull(savedUser, "Saved user should not be null");

        // Step 4: Verify the User properties
        Assertions.assertEquals("Alice Johnson", savedUser.getName(), "User name should be 'Alice Johnson'");
        Assertions.assertEquals("alice.johnson@example.com", savedUser.getEmail(), "User email should be 'alice.johnson@example.com'");

        // Step 5: Verify the associated Message objects
        List<Message> savedMessages = savedUser.getMessages();
        Assertions.assertNotNull(savedMessages, "Messages list should not be null");
        Assertions.assertEquals(2, savedMessages.size(), "User should have 2 messages");

        // Verify properties of each message
        Message savedMessage1 = savedMessages.get(0);
        Assertions.assertEquals("Hello, this is Alice!", savedMessage1.getContent(), "Message 1 content should be 'Hello, this is Alice!'");
        Assertions.assertEquals("Alice Johnson", savedMessage1.getAuthorName(), "Message 1 author name should be 'Alice Johnson'");
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 20, 10, 0), savedMessage1.getTimestamp(), "Message 1 timestamp should be '2023-10-20T10:00:00'");

        Message savedMessage2 = savedMessages.get(1);
        Assertions.assertEquals("Another message from Alice", savedMessage2.getContent(), "Message 2 content should be 'Another message from Alice'");
        Assertions.assertEquals("Alice Johnson", savedMessage2.getAuthorName(), "Message 2 author name should be 'Alice Johnson'");
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 20, 11, 0), savedMessage2.getTimestamp(), "Message 2 timestamp should be '2023-10-20T11:00:00'");
    }

    @Test
    @DisplayName("test: save user with duplicate email throws SQLException")
    public void testSaveUserWithDuplicateEmailThrowsSQLException() throws SQLException {
        User duplicateUser = new User("Duplicate User", "john.doe@example.com");
        Assertions.assertFalse(userDao.saveUser(duplicateUser));
    }

    @Test
    @DisplayName("test: user's message list is empty")
    public void testUserMessageListIsEmpty() throws SQLException {
        User user = new User("Alice Johnson", "alice.johnson@example.com");

        boolean isSaved = userDao.saveUser(user);
        Assertions.assertTrue(isSaved, "User should be saved successfully");

        User savedUser = userDao.getUserByEmail("alice.johnson@example.com");
        Assertions.assertNotNull(savedUser, "Saved user should not be null");

        // Step 4: Verify the User properties
        Assertions.assertEquals("Alice Johnson", savedUser.getName(), "User name should be 'Alice Johnson'");
        Assertions.assertEquals("alice.johnson@example.com", savedUser.getEmail(), "User email should be 'alice.johnson@example.com'");

        // Step 5: Verify the associated Message objects
        List<Message> savedMessages = savedUser.getMessages();
        Assertions.assertNotNull(savedMessages, "Messages list should not be null");
        Assertions.assertTrue(savedMessages.isEmpty(), "User messages list should be empty");
    }

    @Test
    @DisplayName("test: update user details")
    public void testUpdateUserDetails() throws SQLException {
        User user = userDao.getUserByEmail("john.doe@example.com");

        Assertions.assertNotNull(user, "User should not be null");

        user.setName("Johnathan Doe");
        user.setEmail("johnathan.doe@example.com");

        boolean isUpdated = userDao.updateUser(user);
        Assertions.assertTrue(isUpdated, "User should be updated successfully");

        User updatedUser = userDao.getUserByEmail("johnathan.doe@example.com");
        Assertions.assertNotNull(updatedUser, "Updated user should not be null");

        Assertions.assertEquals("Johnathan Doe", updatedUser.getName(), "User name should be 'Johnathan Doe'");
        Assertions.assertEquals("johnathan.doe@example.com", updatedUser.getEmail(), "User email should be 'johnathan.doe@example.com'");

        User oldEmailUser = userDao.getUserByEmail("john.doe@example.com");
        Assertions.assertNull(oldEmailUser, "User with old email should be null");
    }

    @Test
    @DisplayName("test: check if user and their messages has been deleted")
    public void testDeleteUser() throws SQLException {
        User user = userDao.getUserByEmail("john.doe@example.com");

        // Check that the user is not null
        Assertions.assertNotNull(user, "User should not be null");

        // Check user properties
        Assertions.assertEquals(1L, user.getId(), "User ID should be 1");
        Assertions.assertEquals("John Doe", user.getName(), "User name should be 'John Doe'");
        Assertions.assertEquals("john.doe@example.com", user.getEmail(), "User email should be 'john.doe@example.com'");

        // Check the number of messages
        List<Message> messages = user.getMessages();
        Assertions.assertNotNull(messages, "Messages list should not be null");
        Assertions.assertEquals(2, messages.size(), "User should have 2 messages");

        // Delete the user
        boolean isDeleted = userDao.deleteUser("john.doe@example.com");
        Assertions.assertTrue(isDeleted, "User should be deleted successfully");

        // Verify that the user has been deleted
        User deletedUser = userDao.getUserByEmail("john.doe@example.com");
        Assertions.assertNull(deletedUser, "Deleted user should be null");

        // Verify that the messages associated with the user have also been deleted
        for (Message message : messages) {
            Message deletedMessage = messageDao.getMessage(message.getId());
            Assertions.assertNull(deletedMessage, "Deleted message should be null");
        }
    }

}
