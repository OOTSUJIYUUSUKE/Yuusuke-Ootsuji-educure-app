package jp.co.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
	Order findBySessionId(String sessionId);
	
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findOrdersByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);
}
