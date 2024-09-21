package controller;

import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.servlet.api.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
    @InjectMocks
    private OrderController orderController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ServletConfig mockServletConfig = mock(ServletConfig.class);
        ServletContext mockServletContext = mock(ServletContext.class);

        when(mockServletConfig.getServletContext()).thenReturn(mockServletContext);
        when(mockServletContext.getAttribute("orderService")).thenReturn(orderService);

        orderController.init(mockServletConfig);

        when(response.getWriter()).thenReturn(writer);

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

//        mockOrders.add(order1);
//        mockOrders.add(order2);
//
//        when(orderService.getOrders()).thenReturn(mockOrders);
//
//        // Act
//        orderController.doGet(request, response);
//
//        // Assert
//        verify(response).setContentType("application/json");
//        verify(response).setCharacterEncoding("UTF-8");
//        verify(response).setStatus(HttpServletResponse.SC_OK);


//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//
//        orderController.doGet(request, response);
//
//        verify(response).setContentType("text/plain");
//        assertEquals("Hello, World!\n", stringWriter.toString());

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
    }
}

