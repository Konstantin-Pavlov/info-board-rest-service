package com.aston.infoBoardRestService.dao;

import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderDao {
    Logger logger = Logger.getLogger(UserDao.class.getName());

    public List<Order> getOrders() throws SQLException {
        String query = """
                    SELECT * FROM orders;
                """;
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DbUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = getOrder(resultSet);
                logger.info(String.format("Order found: %s", order));
                orders.add(order);
            }
        }
        return orders;
    }

    private Order getOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setTitle(resultSet.getString("title"));
        return order;
    }

}
