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
 * <br><br/>
 * The static block in the DbUtil class loads the application.properties file using the class loader.
 * If the file is not found or an error occurs while loading it, an IOException is thrown, and the application will not start.
 */
public final class DbUtil {
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

    private static String URL = properties.getProperty("db.url");
    private static String USER = properties.getProperty("db.username");
    private static String PASSWORD = properties.getProperty("db.password");
    private static final String DRIVER = properties.getProperty("db.driver");
    private static final String LIQUIBASE_CHANGELOG_PATH = properties.getProperty("liquibase.change_log_file_path");

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
     * Loads the JDBC driver for PostgreSQL.
     *
     * @return a message indicating whether the driver was successfully loaded
     */
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

    /**
     * Retrieves the path to the Liquibase changelog file from the liquibase.properties file.
     *
     * @return the path to the Liquibase changelog file
     */
    public static String getChangelogPath() {
        return LIQUIBASE_CHANGELOG_PATH;
    }

    /**
     * Sets the database connection details dynamically when testing dao layer with test containers.
     *
     * @param url      the database URL
     * @param user     the database username
     * @param password the database password
     */
    public static void setConnectionDetails(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
    }

}
