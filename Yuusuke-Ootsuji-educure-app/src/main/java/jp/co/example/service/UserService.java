package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.example.dao.UserDao;
import jp.co.example.entity.User;

@Service
public class UserService {
	
	@Autowired
    private UserDao userDao;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User authenticate(String userId, String password) {

        User user = userDao.findByUserId(userId);
        
        if (user == null) {
            return null;
        }
        
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        } else {
            return null;
        }
    }
 
    public boolean isUserIdDuplicate(String userId) {
        return userDao.existsByUserId(userId);
    }

    public boolean isTelephoneDuplicate(String telephone) {
        return userDao.existsByTelephone(telephone);
    }
    
    public void registerUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userDao.save(user);
    }
    
    public User getUserById(String userId) {
        return userDao.findByUserId(userId);
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
