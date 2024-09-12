package com.aston.infoBoardRestService.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class for managing database connections.
 * This class implements the Singleton design pattern to ensure that
 * only one instance of the database utility exists throughout the application.
 * It provides a method to obtain a connection to the PostgreSQL database.
 */
public class DbUtil {
    private static final Logger logger = Logger.getLogger(DbUtil.class.getName());
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DbUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.severe("Sorry, unable to find application.properties");
                throw new IOException("application.properties not found");
            }
            properties.load(input);
        } catch (IOException ex) {
            logger.severe("Error loading application.properties: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static final String URL = properties.getProperty("db.url");
    private static final String USER = properties.getProperty("db.username");
    private static final String PASSWORD = properties.getProperty("db.password");
    private static final String DRIVER = properties.getProperty("db.driver");


    // Singleton instance
    private static DbUtil instance;

    // Private constructor to prevent instantiation
    private DbUtil() {
    }

    /**
     * Returns the singleton instance of the DbUtil class.
     * This method is synchronized to ensure thread safety.
     *
     * @return the singleton instance of DbUtil
     */
    public static synchronized DbUtil getInstance() {
        if (instance == null) {
            instance = new DbUtil();
        }
        return instance;
    }

    public static String loadJdbcDriver() {
        try {
            Class.forName(DRIVER);
            return "success - jdbc driver loaded";
        } catch (ClassNotFoundException e) {
            logger.severe(e.getMessage());
            return "jdbc driver failed to load";
        }
    }

    /**
     * Obtains a connection to the PostgreSQL database.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
