package dao;

import com.aston.infoBoardRestService.dao.OrderDao;
import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.util.DbUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

@Testcontainers
public class OrderDaoTest {

    private final Logger logger = Logger.getLogger(OrderDaoTest.class.getName());

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private OrderDao orderDao;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up the data source and inject it into your repository
        DbUtil.setConnectionDetails(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        orderDao = new OrderDao();

        // Initialize the database schema before each test
        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(255) NOT NULL)");
            stmt.execute("TRUNCATE TABLE orders RESTART IDENTITY"); // Ensure clean state
            stmt.execute("INSERT INTO orders (title) VALUES ('val1'), ('val2'), ('val3')"); // Insert sample data
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up the database after each test, if needed
        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE orders RESTART IDENTITY");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @DisplayName("test: get all orders")
    @Test
    public void testGetOrders() throws SQLException {
        List<Order> orders = orderDao.getOrders();

        Assertions.assertNotNull(orders);
        Assertions.assertEquals(3, orders.size());
        Assertions.assertEquals("val1", orders.get(0).getTitle());
        Assertions.assertEquals("val2", orders.get(1).getTitle());
        Assertions.assertEquals("val3", orders.get(2).getTitle());
    }

    @DisplayName("test: get all orders when db is empty")
    @Test
    public void testGetOrdersEmptyDatabase() throws SQLException {
        tearDown(); // Explicitly truncate the table before the test

        List<Order> orders = orderDao.getOrders();
        Assertions.assertTrue(orders.isEmpty());
    }
}
