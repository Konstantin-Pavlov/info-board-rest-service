package com.aston.infoBoardRestService.service.impl;

import com.aston.infoBoardRestService.dao.OrderDao;
import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;

import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private  OrderDao orderDao;

    public OrderServiceImpl() {
        this.orderDao = new OrderDao();
    }

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    @Override
    public List<Order> getOrders() throws SQLException {
        return orderDao.getOrders();
    }
}
