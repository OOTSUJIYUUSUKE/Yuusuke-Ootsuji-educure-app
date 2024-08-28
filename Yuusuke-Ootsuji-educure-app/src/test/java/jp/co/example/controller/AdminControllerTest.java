package jp.co.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.example.entity.Contact;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.Role;
import jp.co.example.entity.User;
import jp.co.example.service.AdminService;
import jp.co.example.service.ContactService;
import jp.co.example.service.OrderService;
import jp.co.example.service.PasswordResetService;
import jp.co.example.service.ProductService;
import jp.co.example.service.RoleService;
import jp.co.example.service.UserService;

@WebMvcTest(controllers = AdminController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AdminControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	@MockBean
	private RoleService roleService;
	
	@MockBean
	private ContactService contactService;

	@MockBean
	private PasswordResetService passwordResetService;

	@MockBean
	private ProductService productService;

	@MockBean
	private OrderService orderService;
	
	@MockBean
	private AdminService adminService;

	@MockBean
	private JavaMailSender mailSender;
	
	@MockBean
    private BCryptPasswordEncoder passwordEncoder;
	
	@Test
	void testGetAdminLogin() throws Exception {
	    mockMvc.perform(get("/admin_login"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_login"))
	            .andExpect(model().attributeExists("loginForm"));
	}
	
	@Test
	void testPostAdminLogin_Success() throws Exception {
	    User adminUser = new User();
	    adminUser.setUserId("admin");
	    adminUser.setRole(new Role(1, "管理者"));

	    doReturn(adminUser).when(adminService).authenticate(anyString(), anyString());

	    mockMvc.perform(post("/admin_login")
	            .param("userId", "admin")
	            .param("password", "password_123"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_dashboard"))
	            .andExpect(request().sessionAttribute("user", adminUser));
	}
	
	@Test
	void testPostAdminLogin_Failure() throws Exception {
	    doReturn(null).when(adminService).authenticate(anyString(), anyString());

	    mockMvc.perform(post("/admin_login")
	            .param("userId", "admin")
	            .param("password", "wrongpassword_123"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_login"))
	            .andExpect(model().attributeExists("authErrorMessage"));
	}

	@Test
	void testGetAdminDashboard_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_dashboard")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_dashboard"));
	}

	@Test
	void testGetAdminDashboard_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_dashboard"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testManageUsers_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    User user1 = new User("user1", "User One", "123456789", "Address 1", "user1@example.com", "password_1", new Role(2, "一般"));
	    User user2 = new User("user2", "User Two", "987654321", "Address 2", "user2@example.com", "password_2", new Role(2, "一般"));
	    List<User> users = List.of(user1, user2);

	    doReturn(users).when(adminService).findUsers(isNull(), isNull());

	    mockMvc.perform(get("/admin_user_management")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_management"))
	            .andExpect(model().attributeExists("userList", "userSearchForm"))
	            .andExpect(model().attribute("userList", users));
	}
	
	@Test
	void testManageUsers_SearchForUserId() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    User user1 = new User("user1", "User One", "123456789", "Address 1", "user1@example.com", "password_1", new Role(2, "一般"));
	    List<User> userList = List.of(user1);

	    doReturn(userList).when(adminService).findUsers(eq("user1"), isNull());

	    mockMvc.perform(get("/admin_user_management")
	            .sessionAttr("user", adminUser)
	            .param("userId", "user1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_management"))
	            .andExpect(model().attributeExists("userList", "userSearchForm"))
	            .andExpect(model().attribute("userList", userList));
	}

	@Test
	void testManageUsers_SearchForEmail() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    User user1 = new User("user1", "User One", "123456789", "Address 1", "user1@example.com", "password_1", new Role(2, "一般"));
	    List<User> userList = List.of(user1);

	    doReturn(userList).when(adminService).findUsers(isNull(), eq("user1@example.com"));

	    mockMvc.perform(get("/admin_user_management")
	            .sessionAttr("user", adminUser)
	            .param("email", "user1@example.com"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_management"))
	            .andExpect(model().attributeExists("userList", "userSearchForm"))
	            .andExpect(model().attribute("userList", userList));
	}
	
	@Test
	void testManageUsers_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_user_management"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	void testShowAdminUserDetail_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    User user = new User();
	    doReturn(user).when(adminService).findByUserId(anyString());

	    mockMvc.perform(get("/admin_user_detail")
	            .sessionAttr("user", adminUser)
	            .param("userId", "testuser"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_detail"))
	            .andExpect(model().attributeExists("user"))
	            .andExpect(model().attribute("user", user));
	}

	@Test
	void testShowAdminUserDetail_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_user_detail")
	            .param("userId", "testuser"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testAdminEditProfile_Success() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    doNothing().when(adminService).updateUser(any(User.class));

	    mockMvc.perform(post("/admin_edit_profile")
	            .sessionAttr("user", adminUser)
	            .param("userId", "testuser")
	            .param("userName", "Updated User"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_edit_profile_result"));
	}
	
	@Test
	void testEditUserProfileResult_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_edit_profile_result")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_edit_profile_result"));
	}

	@Test
	void testEditUserProfileResult_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_edit_profile_result"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testShowAdminUserDeleteConfirm_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));
	    User targetUser = new User();
	    targetUser.setUserId("user1");

	    doReturn(targetUser).when(adminService).findByUserId("user1");

	    mockMvc.perform(get("/admin_user_delete_confirm")
	            .sessionAttr("user", adminUser)
	            .param("userId", "user1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_delete_confirm"))
	            .andExpect(model().attribute("user", targetUser))
	            .andExpect(model().attribute("userId", "user1"));
	}

	@Test
	void testShowAdminUserDeleteConfirm_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_user_delete_confirm")
	            .param("userId", "user1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testAdminUserDelete_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    doNothing().when(adminService).deleteUserByUserId("user1");

	    mockMvc.perform(post("/admin_user_delete_confirm")
	            .sessionAttr("user", adminUser)
	            .param("userId", "user1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_user_delete_result"));
	}

	@Test
	void testAdminUserDelete_WithoutAdminUser() throws Exception {
	    mockMvc.perform(post("/admin_user_delete_confirm")
	            .param("userId", "user1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testAdminUserDeleteResult_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_user_delete_result")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_delete_result"));
	}

	@Test
	void testAdminUserDeleteResult_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_user_delete_result"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testManageProducts_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    List<Product> products = List.of(new Product(), new Product());
	    doReturn(products).when(adminService).findProducts(null, null);

	    mockMvc.perform(get("/admin_product_management")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_management"))
	            .andExpect(model().attributeExists("productList", "productSearchForm"))
	            .andExpect(model().attribute("productList", products));
	}

	@Test
	void testManageProducts_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_product_management"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testShowAdminProductDetail_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    Product product = new Product();
	    doReturn(product).when(adminService).findByProductId(1L);

	    mockMvc.perform(get("/admin_product_detail")
	            .sessionAttr("user", adminUser)
	            .param("productId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_detail"))
	            .andExpect(model().attribute("product", product));
	}

	@Test
	void testShowAdminProductDetail_NoAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_product_detail")
	            .param("productId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testShowAdminProductDeleteConfirm_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    Product product = new Product();
	    product.setProductId(1L);
	    doReturn(product).when(adminService).findByProductId(1L);

	    mockMvc.perform(get("/admin_product_delete_confirm")
	            .sessionAttr("user", adminUser)
	            .param("productId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_delete_confirm"))
	            .andExpect(model().attribute("product", product))
	            .andExpect(model().attribute("productId", 1L));
	}

	@Test
	void testShowAdminProductDeleteConfirm_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_product_delete_confirm")
	            .param("productId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testAdminProductDeleteResult_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_product_delete_result")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_delete_result"));
	}

	@Test
	void testAdminProductDeleteResult_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_product_delete_result"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testManageOrders_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    List<Order> orders = List.of(new Order(), new Order());
	    doReturn(orders).when(adminService).findOrders(null);

	    mockMvc.perform(get("/admin_order_management")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_order_management"))
	            .andExpect(model().attributeExists("orderList", "productNames", "orderSearchForm"))
	            .andExpect(model().attribute("orderList", orders));
	}

	@Test
	void testManageOrders_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_order_management"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testShowAdminOrderDetail_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    Order order = new Order();
	    Product product = new Product();
	    product.setProductName("Test Product");

	    doReturn(order).when(adminService).findByOrderId(1L);
	    doReturn(product).when(adminService).findByProductId(order.getProductId());

	    mockMvc.perform(get("/admin_order_detail")
	            .sessionAttr("user", adminUser)
	            .param("orderId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_order_detail"))
	            .andExpect(model().attribute("order", order))
	            .andExpect(model().attributeExists("productNames"));
	}

	@Test
	void testShowAdminOrderDetail_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_order_detail")
	            .param("orderId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testManageContacts_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    List<Contact> contacts = List.of(new Contact(), new Contact());
	    doReturn(contacts).when(adminService).findContacts(null, null);

	    mockMvc.perform(get("/admin_contact_management")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_contact_management"))
	            .andExpect(model().attributeExists("contactList", "contactSearchForm"))
	            .andExpect(model().attribute("contactList", contacts));
	}

	@Test
	void testManageContacts_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_contact_management"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testShowAdminContactDetail_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    Contact contact = new Contact();
	    doReturn(contact).when(adminService).findByContactId(1L);

	    mockMvc.perform(get("/admin_contact_detail")
	            .sessionAttr("user", adminUser)
	            .param("contactId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_contact_detail"))
	            .andExpect(model().attribute("contact", contact));
	}

	@Test
	void testShowAdminContactDetail_WithouAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_contact_detail")
	            .param("contactId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testGetAdminRegister_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_register")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register"))
	            .andExpect(model().attributeExists("registerForm"));
	}

	@Test
	void testGetAdminRegisterWithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_register"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testPostRegister_Success() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    doReturn(false).when(adminService).isUserIdDuplicate("newuser");
	    doReturn(false).when(adminService).isTelephoneDuplicate("123456789");
	    doNothing().when(adminService).registerUser(any(User.class));

	    mockMvc.perform(post("/admin_register")
	            .sessionAttr("user", adminUser)
	            .param("userId", "newuser")
	            .param("userName", "NewUser")
	            .param("email", "newuser@example.com")
	            .param("address", "New Address")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_register_result"));
	}

	@Test
	void testPostRegister_Failure() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    doReturn(true).when(adminService).isUserIdDuplicate("newuser");

	    mockMvc.perform(post("/admin_register")
	            .sessionAttr("user", adminUser)
	            .param("userId", "newuser")
	            .param("userName", "NewUser")
	            .param("email", "newuser@example.com")
	            .param("address", "New Address")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register"))
	            .andExpect(model().attributeExists("userIdExistsError"));
	}

	@Test
	void testPostRegister_WithoutAdminUser() throws Exception {
	    mockMvc.perform(post("/admin_register")
	            .param("userId", "newuser")
	            .param("userName", "New User")
	            .param("email", "newuser@example.com")
	            .param("address", "New Address")
	            .param("telephone", "123456789")
	            .param("password", "password_123")
	            .param("confirmPassword", "password"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testAdminRegisterResult_WithAdminUser() throws Exception {
	    User adminUser = new User();
	    adminUser.setRole(new Role(1, "管理者"));

	    mockMvc.perform(get("/admin_register_result")
	            .sessionAttr("user", adminUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register_result"));
	}

	@Test
	void testAdminRegisterResult_WithoutAdminUser() throws Exception {
	    mockMvc.perform(get("/admin_register_result"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
}
