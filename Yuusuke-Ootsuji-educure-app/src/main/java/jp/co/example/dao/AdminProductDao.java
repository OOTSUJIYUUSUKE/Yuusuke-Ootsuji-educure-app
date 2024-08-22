package jp.co.example.dao;

import jp.co.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminProductDao extends JpaRepository<Product, Long> {
    List<Product> findByProductIdOrUserId(Long productId, String userId);
    
    Product findByProductId(Long productId);
    
    void deleteByProductId(Long productId);
}
