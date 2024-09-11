package com.aston.infoBoardRestService.servlet;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class LiquibaseServletListener implements ServletContextListener {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            runLiquibase();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Failed to initialize Liquibase", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if necessary
    }

    private void runLiquibase() throws SQLException, LiquibaseException {
        String url = "jdbc:postgresql://localhost:5431/info_board";
        String username = "postgres";
        String password = "postgres";
        String changeLogFile = "db/changelog/master.yaml";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
            Liquibase liquibase = new Liquibase(changeLogFile, resourceAccessor, database);
            liquibase.update(new Contexts(), new LabelExpression());
        }
    }
}


