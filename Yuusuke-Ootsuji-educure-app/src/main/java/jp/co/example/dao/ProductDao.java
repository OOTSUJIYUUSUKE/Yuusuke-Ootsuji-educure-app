package jp.co.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
	
    List<Product> findTop10ByOrderByCreatedAtDesc();
    
    List<Product> findByProductNameContaining(String productName);
    
    @Query("SELECT p FROM Product p WHERE p.userId = :userId ORDER BY p.createdAt DESC")
    List<Product> findProductsByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);
}
