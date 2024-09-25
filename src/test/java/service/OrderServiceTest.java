package service;

import com.aston.infoBoardRestService.dao.OrderDao;
import com.aston.infoBoardRestService.entity.Order;
import com.aston.infoBoardRestService.service.OrderService;
import com.aston.infoBoardRestService.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();
    @Mock
    private OrderDao orderDao;

    private List<Order> mockOrders;

    @BeforeEach
    public void setUp() {
        mockOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTitle("Order 1");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTitle("Order 2");

        mockOrders.add(order1);
        mockOrders.add(order2);
    }

    @Test
    public void test1() throws SQLException {
        Mockito.when(orderService.getOrders()).thenReturn(mockOrders);

        List<Order> orders = orderService.getOrders();

        Assertions.assertEquals(2, orders.size());

    }
}
