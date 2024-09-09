package com.aston.infoBoardRestService.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String URL = "jdbc:postgresql://localhost:5431/info_board";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    // Singleton instance
    private static DbUtil instance;

    // Private constructor to prevent instantiation
    private DbUtil() {
    }

    // Public method to provide access to the singleton instance
    public static synchronized DbUtil getInstance() {
        if (instance == null) {
            instance = new DbUtil();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
