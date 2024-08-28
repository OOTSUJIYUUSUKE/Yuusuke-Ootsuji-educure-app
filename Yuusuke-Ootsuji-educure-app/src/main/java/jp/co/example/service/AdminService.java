package jp.co.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.dao.AdminContactDao;
import jp.co.example.dao.AdminDao;
import jp.co.example.dao.AdminOrderDao;
import jp.co.example.dao.AdminProductDao;
import jp.co.example.dao.AdminUserDao;
import jp.co.example.entity.Contact;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;

@Service
public class AdminService {
	
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	AdminUserDao adminUserDao;
	
	@Autowired
	AdminProductDao adminProductDao;
	
	@Autowired
	AdminOrderDao adminOrderDao;
	
	@Autowired
	AdminContactDao adminContactDao;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public User authenticate(String userId, String password) {

        User user = adminDao.findByUserId(userId);
        
        if (user == null) {
            return null;
        }
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
	
	public List<User> findUsers(String userId, String email) {
        if ((userId == null || userId.isEmpty()) && (email == null || email.isEmpty())) {
            return adminUserDao.findAll();
        } else {
            return adminUserDao.findByUserIdOrEmail(userId, email);
        }
    }
	
	public User findByUserId(String userId) {
		return adminUserDao.findByUserId(userId);
	}
	
	@Transactional
	public void updateUser(User user) {
        User existingUser = adminUserDao.findByUserId(user.getUserId());
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            if (!existingUser.getTelephone().equals(user.getTelephone())) {
                existingUser.setTelephone(user.getTelephone());
            }
            adminUserDao.save(existingUser);
        }
    }
	
	@Transactional
	public void deleteUserByUserId(String userId) {
		adminUserDao.deleteByUserId(userId);
	}
	
	public List<Product> findProducts(Long productId, String userId) {
        if (productId == null && (userId == null || userId.isEmpty())) {
            return adminProductDao.findAll();
        } else {
            return adminProductDao.findByProductIdOrUserId(productId, userId);
        }
    }
	
	public Product findByProductId(Long productId) {
		return adminProductDao.findByProductId(productId);
	}
	
	@Transactional
	public void deleteProductByProductId(Long productId) {
		adminProductDao.deleteByProductId(productId);
	}
	
	public List<Order> findOrders(Long orderId) {
        if (orderId == null) {
            return adminOrderDao.findAll();
        } else {
            return adminOrderDao.findByOrderId(orderId);
        }
    }
	
	public Order findByOrderId(Long orderId) {
		return adminOrderDao.getOrderByOrderId(orderId);
	}
	
	public List<Contact> findContacts(Long contactId, String status) {
		if (contactId == null && (status == null || status.isEmpty())) {
	        return adminContactDao.findAll();
	    } else if (contactId != null && (status == null || status.isEmpty())) {
	        return adminContactDao.findByContactId(contactId);
	    } else if (contactId == null && (status != null && !status.isEmpty())) {
	        return adminContactDao.findByStatus(status);
	    } else {
	        return adminContactDao.findByContactIdAndStatus(contactId, status);
	    }
    }
	
	public Contact findByContactId(Long contactId) {
		return adminContactDao.findContactByContactId(contactId);
	}
 
    public boolean isUserIdDuplicate(String userId) {
        return adminDao.existsByUserId(userId);
    }

    public boolean isTelephoneDuplicate(String telephone) {
        return adminDao.existsByTelephone(telephone);
    }
    
    @Transactional
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        adminDao.save(user);
    }
}
