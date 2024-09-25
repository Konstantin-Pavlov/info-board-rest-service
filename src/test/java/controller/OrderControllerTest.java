package controller;

import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.servlet.api.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private OrderService orderService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PrintWriter writer;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private OrderController orderController;
    private ObjectMapper objectMapper;
    private List<Order> mockOrders;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        orderController.init();

        objectMapper = new ObjectMapper();

        mockOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTitle("Order 1");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTitle("Order 2");

        mockOrders.add(order1);
        mockOrders.add(order2);

        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void getAllOrders() throws Exception {
        Mockito.when(orderService.getOrders()).thenReturn(mockOrders);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        orderController.doGet(request, response);

        Mockito.verify(orderService, Mockito.times(1)).getOrders();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = stringWriter.toString();
        Assertions.assertTrue(jsonResponse.contains("Order 1"));
        Assertions.assertTrue(jsonResponse.contains("Order 2"));
    }

    @Test
    public void getAllOrders_EmptyList() throws SQLException, IOException, ServletException {
        // Mock an empty list of orders
        Mockito.when(orderService.getOrders()).thenReturn(new ArrayList<>());

        // Setup writer to capture the response
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        // Call the doGet method
        orderController.doGet(request, response);

        // Verify the service and status
        Mockito.verify(orderService, Mockito.times(1)).getOrders();
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);

        // Verify the JSON response is an empty array
        String jsonResponse = stringWriter.toString();
        Assertions.assertEquals("[]", jsonResponse.trim());  // Adjust the assertion
    }

    @Test
    public void getAllOrders_Exception() throws Exception {
        Mockito.when(orderService.getOrders()).thenThrow(new RuntimeException("Database error"));
        orderController.doGet(request, response);
        Mockito.verify(response, Mockito.times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void getAllOrders_StatusOK() throws Exception {
        Mockito.when(orderService.getOrders()).thenReturn(mockOrders);
        orderController.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void getAllOrders_JsonFormat() throws Exception {
        Mockito.when(orderService.getOrders()).thenReturn(mockOrders);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        orderController.doGet(request, response);

        String jsonResponse = stringWriter.toString();
        Assertions.assertTrue(jsonResponse.contains("\"id\":1"));
        Assertions.assertTrue(jsonResponse.contains("\"title\":\"Order 1\""));
    }

    @Test
    public void getAllOrders_NullList() throws Exception {
        Mockito.when(orderService.getOrders()).thenReturn(null);
        orderController.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}

