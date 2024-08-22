package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jp.co.example.entity.PasswordReset;

import java.util.Optional;

@Repository
public interface PasswordResetDao extends JpaRepository<PasswordReset, String> {
    
    Optional<PasswordReset> findByResetToken(String resetToken);
    
    void deleteByResetToken(String resetToken);
}
