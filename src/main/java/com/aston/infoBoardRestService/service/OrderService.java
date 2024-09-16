package com.aston.infoBoardRestService.service;

import com.aston.infoBoardRestService.entity.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    List<Order> getOrders() throws SQLException;
}
