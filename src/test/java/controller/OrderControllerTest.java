package controller;

import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.servlet.api.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Mock
    protected ServletContext servletContext;
//    @InjectMocks
    private OrderController orderController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        orderController = Mockito.spy(new OrderController());
//        MockitoAnnotations.openMocks(this);
        when(response.getWriter()).thenReturn(writer);
        lenient().when(servletContext.getAttribute("orderService")).thenReturn(orderService);
        Mockito.doReturn(servletContext).when(orderController).getServletContext();
        orderController.init();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDoGet_success() throws Exception {
        // Arrange
        List<Order> mockOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTitle("Order 1");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTitle("Order 2");

        mockOrders.add(order1);
        mockOrders.add(order2);

        when(orderService.getOrders()).thenReturn(mockOrders);

        // Act
        orderController.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);

    }


    @Test
    public void testDoGet_exception() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock PrintWriter and configure the response mock to return it
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // Create an instance of OrderController with a mocked service
        OrderService mockOrderService = mock(OrderService.class);
//        OrderController orderController = new OrderController(mockOrderService);
        OrderController orderController = new OrderController();
        orderController.init();

        // Simulate an exception during the doGet method call
        doThrow(new RuntimeException("Test exception")).when(mockOrderService).getOrders();

        // Call the doGet method
        orderController.doGet(request, response);

        // Verify that response.setStatus(500) was called
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Verify that the response writer was used to write the error message
        verify(writer).write("{\"error\": \"An error occurred\"}");

        // Verify that response.setContentType("application/json") was called twice
        verify(response, times(2)).setContentType("application/json");
    }
}

