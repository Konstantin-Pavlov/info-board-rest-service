package com.aston.infoBoardRestService.servlet;

import com.aston.infoBoardRestService.dao.MessageDao;
import com.aston.infoBoardRestService.dao.OrderDao;
import com.aston.infoBoardRestService.dao.UserDao;
import com.aston.infoBoardRestService.service.MessageService;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.service.impl.MessageServiceImpl;
import com.aston.infoBoardRestService.service.impl.OrderServiceImpl;
import com.aston.infoBoardRestService.service.impl.UserServiceImpl;
import com.aston.infoBoardRestService.util.DbUtil;
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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class servletListener implements ServletContextListener {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            runLiquibase();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Failed to initialize Liquibase", e);
        }

        OrderDao orderDao = new OrderDao();
        OrderService orderService = new OrderServiceImpl(orderDao);
        servletContextEvent.getServletContext().setAttribute("orderService", orderService);

        MessageDao messageDao = new MessageDao();
        MessageService messageService = new MessageServiceImpl(messageDao);
        servletContextEvent.getServletContext().setAttribute("messageService", messageService);

        UserDao userDao = new UserDao();
        UserService userService = new UserServiceImpl(userDao);
        servletContextEvent.getServletContext().setAttribute("userService", userService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if necessary
    }

    private void runLiquibase() throws SQLException, LiquibaseException {
        String changeLogFile = DbUtil.getChangelogPath();

        log.info(TableUtil.loadJdbcDriver());

        try (Connection connection = DbUtil.getInstance().getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
            try (Liquibase liquibase = new Liquibase(changeLogFile, resourceAccessor, database)) {
                liquibase.update(new Contexts(), new LabelExpression());
            }
        }
    }
}


