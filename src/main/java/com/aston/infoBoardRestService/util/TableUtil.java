package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.entity.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class TableUtil {
    private static final Logger logger = Logger.getLogger(TableUtil.class.getName());
    private static final UserDao userDao = new UserDao();
    private static final MessageDao messageDao = new MessageDao();
    private static final User USER = UserGenerator.generateUser();

    private TableUtil() {
    }

    public static String loadJdbcDriver() {
//        try {
//            Class.forName("org.postgresql.Driver");
//            return "success - jdbc driver loaded";
//        } catch (ClassNotFoundException e) {
//            logger.severe(e.getMessage());
//            e.printStackTrace();
//            return "jdbc driver failed to load";
//        }
        return DbUtil.loadJdbcDriver();
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
                        "timestamp TIMESTAMP NOT NULL)";
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
            userDao.saveUser(USER);
            return "Sample user inserted successfully.";
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return "failed to insert users.";
        }
    }

    // java.sql.Date.valueOf(java.time.LocalDate.now()
    public static String insertSampleMessages() {
        try {
            User user = userDao.getUserByEmail(USER.getEmail());
            messageDao.saveMessage(MessageGenerator.generateMessage(user.getId(), user.getName()));
            return "Sample message inserted successfully.";
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return "Failed to insert sample messages.";
        }

    }
}
