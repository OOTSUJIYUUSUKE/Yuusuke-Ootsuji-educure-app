package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.User;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    
    User findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    boolean existsByTelephone(String telephone);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByEmail(String email);
}
