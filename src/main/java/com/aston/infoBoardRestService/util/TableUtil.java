package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class TableUtil {
    private static final Logger logger = Logger.getLogger(TableUtil.class.getName());
    private static final UserDao userDao = new UserDao();

    private TableUtil() {
    }

    public static String loadJdbcDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            return "success - jdbc driver loaded";
        } catch (ClassNotFoundException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return "jdbc driver failed to load";
        }
    }

    public static String createUsersTableIfNotExists() {
        try (Connection connection = DbUtil.getInstance().getConnection()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet tables = dbMetaData.getTables(null, null, "users", new String[]{"TABLE"});

            if (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                logger.info("Found table: " + tableName);
                return "Table 'users' already exists.";
            } else {
                String createTableSQL = "CREATE TABLE users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL)";
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createTableSQL);
                }
                return "Table 'users' created successfully.";
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return "<p>database failed to load</p>";
        }
    }

    public static String createMessagesTableIfNotExists() {
        try (Connection connection = DbUtil.getInstance().getConnection()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet tables = dbMetaData.getTables(null, null, "messages", new String[]{"TABLE"});

            if (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                logger.info("Found table: " + tableName);
                return "Table 'messages' already exists.";
            } else {
                String createTableSQL = "CREATE TABLE messages (" +
                        "id SERIAL PRIMARY KEY, " +
                        "author_id INTEGER NOT NULL, " +
                        "content VARCHAR(256) NOT NULL, " +
                        "author_name VARCHAR(50) NOT NULL, " +
                        "timestamp DATE NOT NULL)";
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createTableSQL);
                }
                return "Table 'messages' created successfully.";
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return "database failed to load";
        }
    }


    public static String insertSampleUsers() {
        try {
            userDao.saveUser(new User( "Bob", "bob@email.com"));
            userDao.saveUser(new User("Dave", "dave@email.com"));
            userDao.saveUser(new User("Rob", "rob@email.com"));
            userDao.saveUser(new User("Rob", "rob@email.com"));
            userDao.saveUser(new User("alice jones", "alice.jones@example.com"));
            return "Sample users inserted successfully.";
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return "failed to insert users.";
        }
    }

    public static String insertSampleMessages() {
        String insertSQL = "INSERT INTO messages (author_id, content, author_name, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);

            // Add realistic message data
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Hello, this is John Doe. How are you?");
            preparedStatement.setString(3, "john_doe");
            preparedStatement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Hi, this is Jane Smith. Nice to meet you!");
            preparedStatement.setString(3, "jane_smith");
            preparedStatement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Hey, this is Alice Jones. Let's catch up soon.");
            preparedStatement.setString(3, "alice_jones");
            preparedStatement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            connection.commit();

            return "Sample messages inserted successfully.";
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return "Failed to insert sample messages.";
        }
    }


}
