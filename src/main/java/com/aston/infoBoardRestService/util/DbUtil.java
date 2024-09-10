package com.aston.infoBoardRestService.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * This class implements the Singleton design pattern to ensure that
 * only one instance of the database utility exists throughout the application.
 * It provides a method to obtain a connection to the PostgreSQL database.
 */
public class DbUtil {
    private static final String URL = "jdbc:postgresql://localhost:5431/info_board";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

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
