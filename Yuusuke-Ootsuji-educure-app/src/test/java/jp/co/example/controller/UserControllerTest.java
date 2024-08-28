package jp.co.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;
import jp.co.example.service.ContactService;
import jp.co.example.service.OrderService;
import jp.co.example.service.PasswordResetService;
import jp.co.example.service.ProductService;
import jp.co.example.service.RoleService;
import jp.co.example.service.UserService;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerTest {
	
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
	private JavaMailSender mailSender;
	
	@MockBean
    private BCryptPasswordEncoder passwordEncoder;
		
	@Test
    void testGetTop() throws Exception {
        mockMvc.perform(get("/top"))
                .andExpect(status().isOk())
                .andExpect(view().name("top"));
    }
	
	@Test
	void testGetUserRegister() throws Exception {
	    mockMvc.perform(get("/user_register"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_register"))
	            .andExpect(model().attributeExists("registerForm"));
	}
	
	@Test
	void testPostUserRegister_Success() throws Exception {
	    doReturn(false).when(userService).isUserIdDuplicate(any(String.class));
	    doReturn(false).when(userService).isTelephoneDuplicateForRegister(any(String.class));
	    doNothing().when(userService).registerUser(any(User.class));
	    doNothing().when(userService).sendRegistrationEmail(any(User.class));

	    mockMvc.perform(post("/user_register")
	            .param("userId", "testuser")
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("address", "123 Test Street")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(redirectedUrl("/user_register_result"));

	}
	
	@Test
	void testPostUserRegister_UserIdDuplicate() throws Exception {
	    doReturn(true).when(userService).isUserIdDuplicate(any(String.class));

	    mockMvc.perform(post("/user_register")
	            .param("userId", "testuser")
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("address", "123 Test Street")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(view().name("user_register"))
	            .andExpect(model().attributeExists("userIdExistsError"));
	}
	
	@Test
	void testPostUserRegister_TelephoneDuplicate() throws Exception {
	    doReturn(true).when(userService).isTelephoneDuplicateForRegister(any(String.class));

	    mockMvc.perform(post("/user_register")
	            .param("userId", "testuser")
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("address", "123 Test Street")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(view().name("user_register"))
	            .andExpect(model().attributeExists("telephoneExistsError"));
	}
	
	@Test
	void testGetUserLogin() throws Exception {
	    mockMvc.perform(get("/user_login"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_login"))
	            .andExpect(model().attributeExists("loginForm"));
	}
	
	@Test
	void testPostUserLogin_Success() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setUserName("Test User");

	    doReturn(mockUser).when(userService).authenticate("testuser", "password_123");

	    mockMvc.perform(post("/user_login")
	            .param("userId", "testuser")
	            .param("password", "password_123"))
	            .andExpect(redirectedUrl("/product_lineup"));
	}

	
	@Test
	void testPostUserLogin_Failure() throws Exception {
	    doReturn(null).when(userService).authenticate("testuser", "wrongpassword_123");

	    mockMvc.perform(post("/user_login")
	            .param("userId", "testuser")
	            .param("password", "wrongpassword_123"))
	            .andExpect(view().name("user_login"))
	            .andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
	void testShowResetForm() throws Exception {
	    mockMvc.perform(get("/password_reset"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset"))
	            .andExpect(model().attributeExists("passwordResetForm"));
	}
	
	@Test
	void testProcessResetRequest_Success() throws Exception {
	    doReturn(true).when(passwordResetService).checkIfUserExistsByEmail(any(String.class));
	    doReturn("reset-token").when(passwordResetService).createPasswordResetToken(any(String.class));
	    doNothing().when(passwordResetService).sendPasswordResetEmail(any(String.class), any(String.class));

	    mockMvc.perform(post("/password_reset")
	            .param("email", "testuser@example.com"))
	            .andExpect(redirectedUrl("/password_reset_send"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessResetRequest_UserNotFound() throws Exception {
	    doReturn(false).when(passwordResetService).checkIfUserExistsByEmail(any(String.class));

	    mockMvc.perform(post("/password_reset")
	            .param("email", "nonexistent@example.com"))
	            .andExpect(view().name("password_reset"))
	            .andExpect(model().attributeExists("errorMessage"))
	            .andExpect(status().isOk());
	}

	@Test
	void testShowSetNewPasswordForm_ValidToken() throws Exception {
	    doReturn(true).when(passwordResetService).validateResetToken(any(String.class));

	    mockMvc.perform(get("/set_new_password")
	            .param("token", "valid-token"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("set_new_password"))
	            .andExpect(model().attributeExists("newPasswordForm"))
	            .andExpect(model().attribute("token", "valid-token"));
	}
	
	@Test
	void testShowSetNewPasswordForm_InvalidToken() throws Exception {
	    doReturn(false).when(passwordResetService).validateResetToken(any(String.class));

	    mockMvc.perform(get("/set_new_password")
	            .param("token", "invalid-token"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset_error"))
	            .andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
	void testProcessNewPassword_Success() throws Exception {
	    doReturn(true).when(passwordResetService).validateResetToken(any(String.class));
	    doNothing().when(passwordResetService).updatePassword(any(String.class), any(String.class));
	    doNothing().when(passwordResetService).invalidateToken(any(String.class));

	    mockMvc.perform(post("/set_new_password")
	            .param("token", "valid-token")
	            .param("newPassword", "newPassword_123")
	            .param("confirmPassword", "newPassword_123"))
	            .andExpect(redirectedUrl("/password_reset_result"))
	            .andExpect(status().is3xxRedirection());
	}
	
	@Test
	void testProcessNewPassword_PasswordMismatch() throws Exception {
	    mockMvc.perform(post("/set_new_password")
	            .param("token", "valid-token")
	            .param("newPassword", "newPassword_123")
	            .param("confirmPassword", "differentPassword_123"))
	            .andExpect(view().name("set_new_password"))
	            .andExpect(model().attributeExists("errorMessage"))
	            .andExpect(model().attribute("token", "valid-token"))
	            .andExpect(status().isOk());
	}
	
	@Test
	void testShowPasswordResetResult() throws Exception {
	    mockMvc.perform(get("/password_reset_result"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset_result"));
	}
	
	@Test
	void testShowPasswordResetError() throws Exception {
	    mockMvc.perform(get("/password_reset_error"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset_error"));
	}
	
	@Test
	void testLogout() throws Exception {
	    mockMvc.perform(get("/logout"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("logout"))
	            .andExpect(request().sessionAttributeDoesNotExist("user"));
	}
	
	@Test
	void testShowAccessError() throws Exception {
	    mockMvc.perform(get("/access_error"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("access_error"));
	}
	
	@Test
	void testShowMyPage_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/mypage").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("mypage"))
	            .andExpect(model().attribute("loginUserName", "Test User"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	void testShowMyPage_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/mypage"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}
	
	@Test
	void testShowUserProfile_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/user_profile").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_profile"))
	            .andExpect(model().attribute("user", mockUser))
	            .andExpect(model().attribute("loginUserName", "Test User"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	void testShowUserProfile_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/user_profile"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testEditUserProfile_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setUserName("Test User");
	    mockUser.setEmail("testuser@example.com");
	    mockUser.setAddress("123 Test Street");
	    mockUser.setTelephone("1234567890");

	    mockMvc.perform(get("/edit_profile").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("edit_profile"))
	            .andExpect(model().attributeExists("userEditForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	void testEditUserProfile_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/edit_profile"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testUpdateProfile_Success() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setUserName("Test User");

	    doReturn(false).when(userService).isTelephoneDuplicateForEdit(anyString(), anyString());
	    doNothing().when(userService).updateUser(any(User.class));

	    mockMvc.perform(post("/edit_profile")
	            .sessionAttr("user", mockUser)
	            .param("userId", "testuser")
	            .param("userName", "Updated User")
	            .param("email", "updated@example.com")
	            .param("address", "New Address")
	            .param("telephone", "9876543210"))
	            .andExpect(redirectedUrl("/edit_profile_result"))
	            .andExpect(status().is3xxRedirection());
	}


	@Test
	void testUpdateProfile_ValidationErrors() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");

	    mockMvc.perform(post("/edit_profile")
	            .sessionAttr("user", mockUser)
	            .param("userName", "")
	            .param("email", "invalid-email")
	            .param("address", "")
	            .param("telephone", ""))
	            .andExpect(view().name("edit_profile"))
	            .andExpect(status().isOk());
	}

	@Test
	void testShowPurchaseHistory_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setUserName("Test User");

	    List<Order> mockOrders = new ArrayList<>();
	    

	    doReturn(mockOrders).when(orderService).findOrdersByUserIdOrderByCreatedAtDesc(anyString());
	    doReturn(new Product()).when(productService).findProductById(anyLong());

	    mockMvc.perform(get("/buy_history").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("buy_history"))
	            .andExpect(model().attributeExists("products"))
	            .andExpect(model().attributeExists("searchForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}
	
	@Test
	void testShowPurchaseHistory_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/buy_history"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testShowSellHistory_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setUserName("Test User");

	    List<Product> mockProducts = new ArrayList<>();

	    doReturn(mockProducts).when(productService).findProductsByUserIdOrderByCreatedAtDesc(anyString());

	    mockMvc.perform(get("/sell_history").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell_history"))
	            .andExpect(model().attributeExists("products"))
	            .andExpect(model().attributeExists("searchForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testShowSellHistory_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/sell_history"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testShowUserDeleteForm_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/user_delete").sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_delete"))
	            .andExpect(model().attributeExists("userDeleteForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	void testShowUserDeleteForm_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/user_delete"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testDeleteUser_Success() throws Exception {
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String rawPassword = "correctPassword_123";
	    String encodedPassword = encoder.encode(rawPassword);

	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setPassword(encodedPassword);

	    doReturn(true).when(userService).deleteUser("testuser");

	    mockMvc.perform(post("/user_delete")
	            .sessionAttr("user", mockUser)
	            .param("password", rawPassword))
	            .andExpect(redirectedUrl("/user_delete_result"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	void testDeleteUser_PasswordMismatch() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("testuser");
	    mockUser.setPassword("encodedPassword_123");

	    doReturn(mockUser).when(userService).authenticate(anyString(), anyString());
	    doReturn(false).when(passwordEncoder).matches(anyString(), anyString());

	    mockMvc.perform(post("/user_delete")
	            .sessionAttr("user", mockUser)
	            .param("password", "wrongPassword_123"))
	            .andExpect(view().name("user_delete"))
	            .andExpect(model().attributeExists("error"))
	            .andExpect(status().isOk());
	}

	@Test
	void testShowUserDeleteResult() throws Exception {
	    mockMvc.perform(get("/user_delete_result"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_delete_result"));
	}
	
	@Test
	void testShowContactForm_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/contact")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact"))
	            .andExpect(model().attributeExists("loginUserName", "searchForm", "contactForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}
	
	@Test
	void testShowContactForm_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/contact"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testSubmitContactForm_Success() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(post("/contact")
	            .sessionAttr("user", mockUser)
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("subject", "Test Subject")
	            .param("message", "Test Message"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/contact_send"));
	}
	
	@Test
	void testSubmitContactForm_ValidationError() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(post("/contact")
	            .sessionAttr("user", mockUser)
	            .param("userName", "")
	            .param("email", "testuser@example.com")
	            .param("subject", "")
	            .param("message", ""))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact"))
	            .andExpect(model().attributeHasFieldErrors("contactForm", "userName", "subject", "message"))
	            .andExpect(model().attributeExists("userNameError", "subjectError", "messageError"));
	}

	@Test
	void testSubmitContactResult() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/contact_send")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact_send"))
	            .andExpect(model().attributeExists("loginUserName", "searchForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testSubmitContactResult_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/contact_send"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}


}
