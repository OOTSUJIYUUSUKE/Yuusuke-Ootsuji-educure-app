package jp.co.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jp.co.example.dao.UserDao;
import jp.co.example.entity.User;

@Service
public class UserService {
	
	@Autowired
    private UserDao userDao;
	
	@Autowired
    private JavaMailSender mailSender;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User authenticate(String userId, String password) {

        User user = userDao.findByUserId(userId);
        
        if (user == null) {
            return null;
        }
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
 
    public boolean isUserIdDuplicate(String userId) {
        return userDao.existsByUserId(userId);
    }

    public boolean isTelephoneDuplicateForRegister(String telephone) {
        return userDao.existsByTelephone(telephone);
    }
    
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }
    
    public void sendRegistrationEmail(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject("会員登録が完了しました");
        helper.setText("ユーザーID: " + user.getUserId() + "\n" +
                       "ユーザー名: " + user.getUserName() + "\n\n" +
                       "ご登録ありがとうございます！", false);

        mailSender.send(message);
    }
    
    public User getUserById(String userId) {
        return userDao.findByUserId(userId);
    }
    
    public boolean isTelephoneDuplicateForEdit(String telephone, String userId) {
        Optional<User> userWithSameTelephone = userDao.findByTelephone(telephone);
        if (userWithSameTelephone.isPresent()) {
            User foundUser = userWithSameTelephone.get();
            return !foundUser.getUserId().equals(userId);
        }
        return false;
    }
    
    public void updateUser(User user) {
        User existingUser = userDao.findByUserId(user.getUserId());
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            existingUser.setTelephone(user.getTelephone());
            userDao.save(existingUser);
        }
    }
    
    public boolean deleteUser(String userId) {
        User user = userDao.findByUserId(userId);
        if (user != null) {
            userDao.delete(user);
            return true;
        }
        return false;
    }
}
