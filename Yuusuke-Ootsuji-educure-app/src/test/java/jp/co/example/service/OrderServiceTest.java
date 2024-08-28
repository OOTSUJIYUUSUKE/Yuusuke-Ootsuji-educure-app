package jp.co.example.service;

import jp.co.example.dao.OrderDao;
import jp.co.example.dao.ProductDao;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private OrderService orderService;

    private Order order1;
    private Order order2;
    private Order order3;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setUp() {
        // Orderの初期化
        order1 = new Order();
        order1.setOrderId(1L);
        order1.setUserId("user_123");
        order1.setSessionId("session123");
        order1.setPrice(BigDecimal.valueOf(100.00));
        order1.setCreatedAt(LocalDateTime.of(2024, 8, 24, 10, 0));

        order2 = new Order();
        order2.setOrderId(2L);
        order2.setUserId("user_456");
        order2.setSessionId("session456");
        order2.setPrice(BigDecimal.valueOf(200.00));
        order2.setCreatedAt(LocalDateTime.of(2024, 8, 23, 10, 0));

        order3 = new Order();
        order3.setOrderId(3L);
        order3.setUserId("user_789");
        order3.setSessionId("session789");
        order3.setPrice(BigDecimal.valueOf(300.00));
        order3.setCreatedAt(LocalDateTime.of(2024, 8, 22, 10, 0));

        // Productの初期化
        product1 = new Product();
        product1.setProductId(1L);
        product1.setUserId("user_123");
        product1.setProductName("トマト");

        product2 = new Product();
        product2.setProductId(2L);
        product2.setUserId("user_456");
        product2.setProductName("キュウリ");

        product3 = new Product();
        product3.setProductId(3L);
        product3.setUserId("user_789");
        product3.setProductName("キャベツ");
    }

    @Test
    public void saveOrder_正常に注文を保存できる() {
        doReturn(order1).when(orderDao).save(order1);

        Order result = orderService.saveOrder(order1);

        assertThat(result).isEqualTo(order1);
        verify(orderDao, times(1)).save(order1);
    }

    @Test
    public void getAllOrders_全ての注文を取得できる() {
        doReturn(Arrays.asList(order1, order2, order3)).when(orderDao).findAll();

        List<Order> result = orderService.getAllOrders();

        assertThat(result).hasSize(3);
        verify(orderDao, times(1)).findAll();
    }

    @Test
    public void getOrderById_注文IDで注文を取得できる() {
        doReturn(Optional.of(order1)).when(orderDao).findById(1L);

        Order result = orderService.getOrderById(1L);

        assertThat(result).isEqualTo(order1);
        verify(orderDao, times(1)).findById(1L);
    }

    @Test
    public void deleteOrder_注文を削除できる() {
        doNothing().when(orderDao).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderDao, times(1)).deleteById(1L);
    }

    @Test
    public void findOrderBySessionId_セッションIDで注文を取得できる() {
        doReturn(order1).when(orderDao).findBySessionId("session123");

        Order result = orderService.findOrderBySessionId("session123");

        assertThat(result).isEqualTo(order1);
        verify(orderDao, times(1)).findBySessionId("session123");
    }

    @Test
    public void getProductNameById_商品IDで商品名を取得できる() {
        doReturn(Optional.of(product1)).when(productDao).findById(1L);

        String result = orderService.getProductNameById(1L);

        assertThat(result).isEqualTo("トマト");
        verify(productDao, times(1)).findById(1L);
    }

    @Test
    public void findOrdersByUserIdOrderByCreatedAtDesc_ユーザーIDで注文を取得できる() {
        doReturn(Arrays.asList(order1, order2)).when(orderDao).findOrdersByUserIdOrderByCreatedAtDesc("user_123");

        List<Order> result = orderService.findOrdersByUserIdOrderByCreatedAtDesc("user_123");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCreatedAt()).isAfter(result.get(1).getCreatedAt());
        verify(orderDao, times(1)).findOrdersByUserIdOrderByCreatedAtDesc("user_123");
    }
}
