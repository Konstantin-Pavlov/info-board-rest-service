package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.util.TableUtil;
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
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
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

        log.info(TableUtil.loadJdbcDriver());

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());

            // Temporary code to test resource loading
//            try (InputStream inputStream = resourceAccessor.openStream(null, changeLogFile)) {
//                if (inputStream == null) {
//                    throw new RuntimeException("Changelog file not found: " + changeLogFile);
//                } else {
//                    log.info("Changelog file found: " + changeLogFile);
//                }
//            }

            try (Liquibase liquibase = new Liquibase(changeLogFile, resourceAccessor, database)) {
                liquibase.update(new Contexts(), new LabelExpression());
            }
        }
    }
}


