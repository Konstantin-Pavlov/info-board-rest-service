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

@WebServlet(name = "order controller", value = "/api/orders/*")
public class OrderController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());
    private  OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        final Object o = getServletContext().getAttribute("orderService");
        this.orderService = (OrderService) o;
    }

    //     Constructor injection
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
//        req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, response);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            List<Order> orders = orderService.getOrders();
            logger.log(Level.INFO, "Retrieved messages: {0}", objectMapper.writeValueAsString(orders.size()));
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), orders);
        } catch (SQLException e) {
            logger.severe(String.format("SQL Exception: %s", e.getMessage()));
            throw new RuntimeException(e);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"An error occurred\"}");
        }

    }
}
