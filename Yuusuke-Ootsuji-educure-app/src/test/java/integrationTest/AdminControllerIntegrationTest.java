package integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jakarta.transaction.Transactional;
import jp.co.example.YuusukeOotsujiEducureAppApplication;
import jp.co.example.entity.User;
import jp.co.example.service.UserService;

@SpringBootTest(classes = YuusukeOotsujiEducureAppApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private UserService userService;
	
	@Test
	@Transactional
	void testGetAdminLogin() throws Exception {
	    mockMvc.perform(get("/admin_login"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_login"))
	            .andExpect(model().attributeExists("loginForm"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostAdminLogin_Success() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/admin_login")
	            .param("userId", user.getUserId())
	            .param("password", "tanaka_100"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_dashboard"))
	            .andExpect(request().sessionAttribute("user", user));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testPostAdminLogin_Failure() throws Exception {
		User user = userService.getUserById("sato_678");

	    mockMvc.perform(post("/admin_login")
	            .param("userId", user.getUserId())
	            .param("password", "sato_678"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_login"))
	            .andExpect(model().attributeExists("authErrorMessage"));
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testGetAdminDashboard_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_dashboard")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_dashboard"));
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testGetAdminDashboard_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_dashboard")
	    		.sessionAttr("user", user))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testManageUsers_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

		mockMvc.perform(get("/admin_user_management")
				.sessionAttr("user", user))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_user_management"))
				.andExpect(model().attributeExists("userList", "userSearchForm"));
	}
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testManageUsers_SearchForUserId() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_user_management")
	            .sessionAttr("user", user)
	            .param("userId", "suzuki"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_management"))
	            .andExpect(model().attributeExists("userList", "userSearchForm"));
	}

	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
	void testManageUsers_SearchForEmail() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_user_management")
	            .sessionAttr("user", user)
	            .param("email", "tanaka_12345@example.com"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_management"))
	            .andExpect(model().attributeExists("userList", "userSearchForm"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testManageUsers_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_user_management")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testShowAdminUserDetail_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_user_detail")
	            .sessionAttr("user", user)
	            .param("userId", "sato_678"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_detail"))
	            .andExpect(model().attributeExists("user"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testShowAdminUserDetail_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_user_detail")
	    		.sessionAttr("user", user)
	            .param("userId", "sato_678"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminEditProfile_Success() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/admin_edit_profile")
	            .sessionAttr("user", user)
	            .param("userId", "sato_678")
	            .param("userName", "佐藤三郎"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_edit_profile_result"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testEditUserProfileResult_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_edit_profile_result")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_edit_profile_result"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testEditUserProfileResult_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_edit_profile_result")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testShowAdminUserDeleteConfirm_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_user_delete_confirm")
	            .sessionAttr("user", user)
	            .param("userId", "suzuki_234"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_delete_confirm"))
	            .andExpect(model().attribute("userId", "suzuki_234"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testShowAdminUserDeleteConfirm_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_user_delete_confirm")
	    		.sessionAttr("user", user)
	            .param("userId", "suzuki_234"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminUserDelete_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(post("/admin_user_delete_confirm")
	            .sessionAttr("user", user)
	            .param("userId", "suzuki_234"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/admin_user_delete_result"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminUserDelete_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(post("/admin_user_delete_confirm")
	    		.sessionAttr("user", user)
	            .param("userId", "suzuki_234"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminUserDeleteResult_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_user_delete_result")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_user_delete_result"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminUserDeleteResult_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_user_delete_result")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testManageProducts_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_product_management")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_management"))
	            .andExpect(model().attributeExists("productList", "productSearchForm"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testManageProducts_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_product_management")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	void testShowAdminProductDetail_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_product_detail")
	            .sessionAttr("user", user)
	            .param("productId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_detail"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	void testShowAdminProductDetail_NoAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_product_detail")
	    		.sessionAttr("user", user)
	            .param("productId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminProductDeleteConfirm_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_product_delete_confirm")
	            .sessionAttr("user", user)
	            .param("productId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_delete_confirm"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminProductDeleteConfirm_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_product_delete_confirm")
	    		.sessionAttr("user", user)
	            .param("productId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testAdminProductDeleteResult_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");
		
	    mockMvc.perform(get("/admin_product_delete_result")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_product_delete_result"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml" })
	@Transactional
	@DirtiesContext
	void testAdminProductDeleteResult_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_product_delete_result")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml", "datasets/orders.yml" })
	@Transactional
	@DirtiesContext
	void testManageOrders_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");
		
	    mockMvc.perform(get("/admin_order_management")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_order_management"))
	            .andExpect(model().attributeExists("orderList", "productNames", "orderSearchForm"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml", "datasets/orders.yml" })
	@Transactional
	@DirtiesContext
	void testManageOrders_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_order_management")
	    		.sessionAttr("user", user))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml", "datasets/orders.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminOrderDetail_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_order_detail")
	            .sessionAttr("user", user)
	            .param("orderId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_order_detail"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/products.yml", "datasets/orders.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminOrderDetail_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_order_detail")
	    		.sessionAttr("user", user)
	            .param("orderId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/contact.yml" })
	@Transactional
	@DirtiesContext
	void testManageContacts_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_contact_management")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_contact_management"))
	            .andExpect(model().attributeExists("contactList", "contactSearchForm"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/contact.yml" })
	@Transactional
	@DirtiesContext
	void testManageContacts_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_contact_management")
	    		.sessionAttr("user", user))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/contact.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminContactDetail_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_contact_detail")
	            .sessionAttr("user", user)
	            .param("contactId", "1"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_contact_detail"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml", "datasets/contact.yml" })
	@Transactional
	@DirtiesContext
	void testShowAdminContactDetail_WithouAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(get("/admin_contact_detail")
	    		.sessionAttr("user", user)
	            .param("contactId", "1"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
	
	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml"})
	@Transactional
	void testGetAdminRegister_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_register")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register"))
	            .andExpect(model().attributeExists("registerForm"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testGetAdminRegisterWithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_register")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testPostRegister_Success() throws Exception {
		User user = userService.getUserById("tanaka_123");
		
	    mockMvc.perform(post("/admin_register")
	            .sessionAttr("user", user)
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
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testPostRegister_Failure() throws Exception {
		User user = userService.getUserById("tanaka_123");
		
	    mockMvc.perform(post("/admin_register")
	            .sessionAttr("user", user)
	            .param("userId", "sato_678")
	            .param("userName", "NewUser")
	            .param("email", "newuser@example.com")
	            .param("address", "New Address")
	            .param("telephone", "123456789")
	            .param("password", "password_123")
	            .param("confirmPassword", "password_123"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testPostRegister_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");
		
	    mockMvc.perform(post("/admin_register")
	    		.sessionAttr("user", user)
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
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminRegisterResult_WithAdminUser() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/admin_register_result")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("admin_register_result"));
	}

	@Test
	@DataSet({ "datasets/role.yml", "datasets/users.yml" })
	@Transactional
	void testAdminRegisterResult_WithoutAdminUser() throws Exception {
		User user = userService.getUserById("sato_678");

		mockMvc.perform(get("/admin_register_result")
				.sessionAttr("user", user))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/access_error"));
	}
}
