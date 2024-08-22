package jp.co.example.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.example.dao.PasswordResetDao;
import jp.co.example.dao.UserDao;
import jp.co.example.entity.PasswordReset;
import jp.co.example.entity.User;

@Service
public class PasswordResetService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordResetDao passwordResetDao;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.reset.link.url}")
    private String resetLinkUrl;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean checkIfUserExistsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    public String createPasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        Optional<User> userOptional = userDao.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        String userId = userOptional.get().getUserId();

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setResetToken(token);
        passwordReset.setUserId(userId);
        passwordReset.setExpiresAt(expiresAt);

        saveResetToken(passwordReset);

        return token;
    }

    @Transactional
    public void saveResetToken(PasswordReset passwordReset) {
        passwordResetDao.save(passwordReset);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetLink = resetLinkUrl + "?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("パスワードリセットのご案内");
        message.setText("パスワードリセットのリンク: " + resetLink + "\n\n" +
                        "このリンクをクリックして、新しいパスワードを設定してください。");
        mailSender.send(message);
    }

    public boolean validateResetToken(String token) {
        return passwordResetDao.findByResetToken(token)
                .map(resetRecord -> LocalDateTime.now().isBefore(resetRecord.getExpiresAt()))
                .orElse(false);
    }

    public void invalidateToken(String token) {
        passwordResetDao.deleteByResetToken(token);
    }
    
    public Optional<User> getUserByResetToken(String token) {
        return passwordResetDao.findByResetToken(token)
            .flatMap(passwordReset -> Optional.ofNullable(userDao.findByUserId(passwordReset.getUserId())));
    }
    
    @Transactional
    public void updatePassword(String token, String newPassword) {
        Optional<User> userOptional = getUserByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userDao.save(user);

            passwordResetDao.deleteByResetToken(token);
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

}
