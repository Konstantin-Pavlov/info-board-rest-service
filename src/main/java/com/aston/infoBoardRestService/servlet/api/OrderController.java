package com.aston.infoBoardRestService.servlet.api;

import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "order controller", value = "/api/orders")
public class OrderController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private final OrderService orderService =  new OrderServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, resp);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");



        try {
            List<Order> orders = orderService.getOrders();
            logger.log(Level.INFO, "Retrieved messages: {0}", objectMapper.writeValueAsString(orders.size()));
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), orders);
        } catch (SQLException e) {
            logger.severe(String.format("SQL Exception: %s", e.getMessage()));
            throw new RuntimeException(e);
        }

    }
}
