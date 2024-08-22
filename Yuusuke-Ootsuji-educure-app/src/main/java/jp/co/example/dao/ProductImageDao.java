package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jp.co.example.entity.ProductImage;

@Repository
public interface ProductImageDao extends JpaRepository<ProductImage, Long> {
}
