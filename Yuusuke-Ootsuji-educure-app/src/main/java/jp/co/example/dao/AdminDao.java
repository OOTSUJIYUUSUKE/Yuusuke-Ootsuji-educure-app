package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.User;

@Repository
public interface AdminDao extends JpaRepository<User, Long> {
    
    User findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    boolean existsByTelephone(String telephone);    
}
