package integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jakarta.transaction.Transactional;
import jp.co.example.YuusukeOotsujiEducureAppApplication;
import jp.co.example.entity.User;
import jp.co.example.service.ContactService;
import jp.co.example.service.OrderService;
import jp.co.example.service.PasswordResetService;
import jp.co.example.service.UserService;

@SpringBootTest(classes = YuusukeOotsujiEducureAppApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    UserService userService;
    
    @Autowired
    PasswordResetService passwordResetService;
    
    @Autowired
    ContactService contactService;
    
    @Autowired
    OrderService orderService;
    
    @Test
    @Transactional
    void testGetTop() throws Exception {
        mockMvc.perform(get("/top"))
                .andExpect(status().isOk())
                .andExpect(view().name("top"));
    }

    @Test
    @Transactional
    void testGetUserRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user_register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_register"))
                .andExpect(model().attributeExists("registerForm"));
    }
    
    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostUserRegister_Success() throws Exception {
	    mockMvc.perform(post("/user_register")
	            .param("userId", "testuser")
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("address", "123 Test Street")
	            .param("telephone", "1234567890")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(redirectedUrl("/user_register_result"));
	    
	    User registeredUser = userService.getUserById("testuser");

	    assertNotNull(registeredUser);
	    assertNotNull(registeredUser.getRole());
	    assertEquals(2, registeredUser.getRole().getRoleId());
	}
    
    @Test
    @Transactional
	void testPostUserRegister_UserIdDuplicate() throws Exception {
    	mockMvc.perform(post("/user_register")
	            .param("userId", "tanaka_123")
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
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostUserRegister_TelephoneDuplicate() throws Exception {
    	mockMvc.perform(post("/user_register")
	            .param("userId", "testuser")
	            .param("userName", "Test User")
	            .param("email", "testuser@example.com")
	            .param("address", "123 Test Street")
	            .param("telephone", "09012345678")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(view().name("user_register"))
	            .andExpect(model().attributeExists("telephoneExistsError"));
	}
    
    @Test
    @Transactional
	void testGetUserLogin() throws Exception {
	    mockMvc.perform(get("/user_login"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_login"))
	            .andExpect(model().attributeExists("loginForm"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostUserLogin_Success() throws Exception {
	    mockMvc.perform(post("/user_login")
	            .param("userId", "tanaka_123")
	            .param("password", "tanaka_100"))
	            .andExpect(redirectedUrl("/product_lineup"));
	}

	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostUserLogin_Failure() throws Exception {
	    mockMvc.perform(post("/user_login")
	            .param("userId", "tanaka_123")
	            .param("password", "wrongpassword_123"))
	            .andExpect(view().name("user_login"))
	            .andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
    @Transactional
	void testShowResetForm() throws Exception {
	    mockMvc.perform(get("/password_reset"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset"))
	            .andExpect(model().attributeExists("passwordResetForm"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testProcessResetRequest_Success() throws Exception {
	    mockMvc.perform(post("/password_reset")
	            .param("email", "tanaka_12345@example.com"))
	            .andExpect(redirectedUrl("/password_reset_send"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testProcessResetRequest_UserNotFound() throws Exception {
	    mockMvc.perform(post("/password_reset")
	            .param("email", "nonexistent@example.com"))
	            .andExpect(view().name("password_reset"))
	            .andExpect(model().attributeExists("errorMessage"))
	            .andExpect(status().isOk());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/password_reset.yml"})
    @Transactional
	void testShowSetNewPasswordForm_ValidToken() throws Exception {
	    mockMvc.perform(get("/set_new_password")
	            .param("token", "valid-token"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("set_new_password"))
	            .andExpect(model().attributeExists("newPasswordForm"))
	            .andExpect(model().attribute("token", "valid-token"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/password_reset.yml"})
	@Transactional
	void testShowSetNewPasswordForm_InvalidToken() throws Exception {
		mockMvc.perform(get("/set_new_password")
				.param("token", "invalid-token"))
				.andExpect(status().isOk())
				.andExpect(view().name("password_reset_error"))
				.andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/password_reset.yml"})
    @Transactional
	void testProcessNewPassword_Success() throws Exception {
	    mockMvc.perform(post("/set_new_password")
	            .param("token", "valid-token")
	            .param("newPassword", "newPassword_123")
	            .param("confirmPassword", "newPassword_123"))
	            .andExpect(redirectedUrl("/password_reset_result"))
	            .andExpect(status().is3xxRedirection());
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/password_reset.yml"})
    @Transactional
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
	@Transactional
	void testShowPasswordResetResult() throws Exception {
	    mockMvc.perform(get("/password_reset_result"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset_result"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowPasswordResetError() throws Exception {
	    mockMvc.perform(get("/password_reset_error"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("password_reset_error"));
	}
	
	@Test
	@Transactional
	void testLogout() throws Exception {
	    mockMvc.perform(get("/logout"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("logout"))
	            .andExpect(request().sessionAttributeDoesNotExist("user"));
	}
	
	@Test
	@Transactional
	void testShowAccessError() throws Exception {
	    mockMvc.perform(get("/access_error"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("access_error"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowMyPage_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/mypage").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("mypage"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	@Transactional
	void testShowMyPage_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/mypage"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowUserProfile_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/user_profile").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_profile"))
	            .andExpect(model().attribute("user", user))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	@Transactional
	void testShowUserProfile_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/user_profile"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testEditUserProfile_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/edit_profile").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("edit_profile"))
	            .andExpect(model().attributeExists("userEditForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	@Transactional
	void testEditUserProfile_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/edit_profile"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testUpdateProfile_Success() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/edit_profile")
	            .sessionAttr("user", user)
	            .param("userId", "testuser")
	            .param("userName", "Updated User")
	            .param("email", "updated@example.com")
	            .param("address", "New Address")
	            .param("telephone", "9876543210"))
	            .andExpect(redirectedUrl("/edit_profile_result"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testUpdateProfile_ValidationErrors() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/edit_profile")
	            .sessionAttr("user", user)
	            .param("userName", "")
	            .param("email", "invalid-email")
	            .param("address", "")
	            .param("telephone", ""))
	            .andExpect(view().name("edit_profile"))
	            .andExpect(status().isOk());
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml", "datasets/orders.yml"})
	@Transactional
	@DirtiesContext
	void testShowPurchaseHistory_UserLoggedIn() throws Exception {
	    User user = userService.getUserById("tanaka_123");

	    MvcResult result = mockMvc.perform(get("/buy_history").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("buy_history"))
	            .andExpect(model().attributeExists("products"))
	            .andExpect(model().attributeExists("searchForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andReturn();

	    List<?> products = (List<?>) result.getModelAndView().getModel().get("products");
	    assertEquals(1, products.size());
	}

	@Test
	@Transactional
	void testShowPurchaseHistory_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/buy_history"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
	@Transactional
	@DirtiesContext
	void testShowSellHistory_UserLoggedIn() throws Exception {
	    User user = userService.getUserById("tanaka_123");

	    MvcResult result = mockMvc.perform(get("/sell_history").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell_history"))
	            .andExpect(model().attributeExists("products"))
	            .andExpect(model().attributeExists("searchForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andReturn();

	    List<?> products = (List<?>) result.getModelAndView().getModel().get("products");
	    assertEquals(3, products.size());
	}

	@Test
	@Transactional
	void testShowSellHistory_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/sell_history"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowUserDeleteForm_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/user_delete").sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_delete"))
	            .andExpect(model().attributeExists("userDeleteForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"))
	            .andExpect(model().attributeExists("searchForm"));
	}

	@Test
	@Transactional
	void testShowUserDeleteForm_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/user_delete"))
	            .andExpect(redirectedUrl("/access_error"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testDeleteUser_Success() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/user_delete")
	            .sessionAttr("user", user)
	            .param("password", "tanaka_100"))
	            .andExpect(redirectedUrl("/user_delete_result"))
	            .andExpect(status().is3xxRedirection());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testDeleteUser_PasswordMismatch() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/user_delete")
	            .sessionAttr("user", user)
	            .param("password", "wrongPassword_123"))
	            .andExpect(view().name("user_delete"))
	            .andExpect(model().attributeExists("error"))
	            .andExpect(status().isOk());
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowUserDeleteResult() throws Exception {
	    mockMvc.perform(get("/user_delete_result"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("user_delete_result"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testShowContactForm_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/contact")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact"))
	            .andExpect(model().attributeExists("loginUserName", "searchForm", "contactForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"));
	}
	
	@Test
	@Transactional
	void testShowContactForm_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/contact"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/contact.yml"})
	@Transactional
	void testSubmitContactForm_Success() throws Exception {
	    User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/contact")
	            .sessionAttr("user", user)
	            .param("userName", "田中太郎")
	            .param("email", "tanaka_12345@example.com")
	            .param("subject", "Test Subject")
	            .param("message", "Test Message"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/contact_send"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/contact.yml"})
	@Transactional
	void testSubmitContactForm_ValidationError() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/contact")
	            .sessionAttr("user", user)
	            .param("userName", "")
	            .param("email", "test_address@example.com")
	            .param("subject", "")
	            .param("message", ""))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact"))
	            .andExpect(model().attributeHasFieldErrors("contactForm", "userName", "subject", "message"))
	            .andExpect(model().attributeExists("userNameError", "subjectError", "messageError"));
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testSubmitContactResult() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/contact_send")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("contact_send"))
	            .andExpect(model().attributeExists("loginUserName", "searchForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"));
	}

	@Test
	@Transactional
	void testSubmitContactResult_UserNotLoggedIn() throws Exception {
	    mockMvc.perform(get("/contact_send"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
}
