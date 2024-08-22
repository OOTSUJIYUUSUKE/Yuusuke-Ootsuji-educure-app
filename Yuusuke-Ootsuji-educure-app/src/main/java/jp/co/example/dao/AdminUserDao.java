package jp.co.example.dao;

import jp.co.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserDao extends JpaRepository<User, Long> {
	
    List<User> findByUserIdOrEmail(String userId, String email);
    
    User findByUserId(String userId);
    
    void deleteByUserId(String userId);
}
