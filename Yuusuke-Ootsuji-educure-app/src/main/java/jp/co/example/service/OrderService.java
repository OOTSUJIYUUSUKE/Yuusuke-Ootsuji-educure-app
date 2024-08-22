package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.dao.OrderDao;
import jp.co.example.dao.ProductDao;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    public Order saveOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        return orderDao.save(order);
    }

    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderDao.findById(orderId).orElse(null);
    }

    public void deleteOrder(Long orderId) {
        orderDao.deleteById(orderId);
    }
    
    public Order findOrderById(Long orderId) {
        return orderDao.findById(orderId).orElse(null);
    }
    
    public Order findOrderBySessionId(String sessionId) {
        return orderDao.findBySessionId(sessionId);
    }
    
    public String getProductNameById(Long productId) {
        return productDao.findById(productId).map(Product::getProductName).orElse("商品名不明");
    }
    
    public List<Order> findOrdersByUserIdOrderByCreatedAtDesc(String userId) {
        return orderDao.findOrdersByUserIdOrderByCreatedAtDesc(userId);
    }
}
