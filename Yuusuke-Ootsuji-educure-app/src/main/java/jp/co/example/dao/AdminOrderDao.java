package jp.co.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.Order;

@Repository
public interface AdminOrderDao extends JpaRepository<Order, Long> {
    List<Order> findByOrderId(Long orderId);
    
    Order getOrderByOrderId(Long orderId);
    
}
