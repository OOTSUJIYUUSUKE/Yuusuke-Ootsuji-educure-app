package jp.co.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.example.controller.form.ContactSearchForm;
import jp.co.example.controller.form.LoginForm;
import jp.co.example.controller.form.OrderSearchForm;
import jp.co.example.controller.form.ProductSearchForm;
import jp.co.example.controller.form.RegisterForm;
import jp.co.example.controller.form.UserSearchForm;
import jp.co.example.entity.Contact;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.Role;
import jp.co.example.entity.User;
import jp.co.example.service.AdminService;
import jp.co.example.service.RoleService;
import jp.co.example.service.UserService;

@Controller
public class AdminController {

	@Autowired
	HttpSession session;

	@Autowired
	UserService userService;

	@Autowired
	AdminService adminService;
	
	@Autowired
	RoleService roleService;

	@GetMapping("/admin_login")
	public String getAdminLogin(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "admin_login";
	}

	@PostMapping("/admin_login")
	public String postAdminLogin(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "user_login";
		}

		User user = adminService.authenticate(loginForm.getUserId(), loginForm.getPasswordHash());
		if (user == null) {
			model.addAttribute("errorMessage", "ユーザーIDかパスワードが間違っています");
			return "admin_login";
		}

		session.setAttribute("user", user);
		return "redirect:/admin_dashboard";
	}

	@GetMapping("/admin_dashboard")
	public String getAdminDashbooard() {
		return "admin_dashboard";
	}

	@GetMapping("/admin_user_management")
	public String manageUsers(@ModelAttribute("userSearchForm") UserSearchForm userSearchForm, Model model) {
		List<User> users = adminService.findUsers(userSearchForm.getUserId(), userSearchForm.getEmail());
		model.addAttribute("userList", users);
		model.addAttribute("userSearchForm", new UserSearchForm());
		return "admin_user_management";
	}

	@GetMapping("/admin_user_detail")
	public String showAdminUserDetail(@RequestParam("userId") String userId, Model model) {
		User user = adminService.findByUserId(userId);

		if (user != null) {
			model.addAttribute("user", user);
		}
		return "admin_user_detail";
	}

	@GetMapping("/admin_edit_profile")
	public String showAdminEditProfile(@RequestParam("userId") String userId, Model model) {
		User user = adminService.findByUserId(userId);

		if (user != null) {
			model.addAttribute("user", user);
		}
		return "admin_edit_profile";
	}

	@PostMapping("/admin_edit_profile")
	public String AdminEditProfile(User user, Model model) {
		adminService.updateUser(user);

		return "redirect:/admin_edit_profile_result";
	}

	@GetMapping("/admin_edit_profile_result")
	public String editUserProfileResult() {
		return "admin_edit_profile_result";
	}

	@GetMapping("/admin_user_delete_confirm")
	public String showAdminUserDeleteConfirm(@RequestParam("userId") String userId, Model model) {
		User user = adminService.findByUserId(userId);

		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("userId", user.getUserId());
		}
		return "admin_user_delete_confirm";
	}

	@PostMapping("/admin_user_delete_confirm")
	public String AdminUserDelete(@RequestParam("userId") String userId, Model model) {

		adminService.deleteUserByUserId(userId);

		return "redirect:/admin_user_delete_result";
	}

	@GetMapping("/admin_user_delete_result")
	public String adminUserDeleteResult() {
		return "admin_user_delete_result";
	}

	@GetMapping("/admin_product_management")
	public String manageProducts(@ModelAttribute("productSearchForm") ProductSearchForm productSearchForm,
			Model model) {
		List<Product> products = adminService.findProducts(productSearchForm.getProductId(),
				productSearchForm.getUserId());
		model.addAttribute("productList", products);
		model.addAttribute("productSearchForm", new ProductSearchForm());
		return "admin_product_management";
	}

	@GetMapping("/admin_product_detail")
	public String showAdminProductDetail(@RequestParam("productId") Long productId, Model model) {
		Product product = adminService.findByProductId(productId);

		if (product != null) {
			model.addAttribute("product", product);
		}
		return "admin_product_detail";
	}

	@GetMapping("/admin_product_delete_confirm")
	public String showAdminProductDeleteConfirm(@RequestParam("productId") Long productId, Model model) {
		Product product = adminService.findByProductId(productId);

		if (product != null) {
			model.addAttribute("product", product);
			model.addAttribute("productId", product.getProductId());
		}
		return "admin_product_delete_confirm";
	}

	@PostMapping("/admin_product_delete_confirm")
	public String AdminProductDelete(@RequestParam("productId") Long productId, Model model) {

		adminService.deleteProductByProductId(productId);

		return "redirect:/admin_product_delete_result";
	}

	@GetMapping("/admin_product_delete_result")
	public String adminProductDeleteResult() {
		return "admin_product_delete_result";
	}

	@GetMapping("/admin_order_management")
	public String manageOrders(@ModelAttribute("orderSearchForm") OrderSearchForm orderSearchForm, Model model) {
		List<Order> orders = adminService.findOrders(orderSearchForm.getOrderId());

		Map<Long, String> productNames = new HashMap<>();
		for (Order order : orders) {
			Product product = adminService.findByProductId(order.getProductId());
			if (product != null) {
				productNames.put(order.getOrderId(), product.getProductName());
			}
		}

		model.addAttribute("orderList", orders);
		model.addAttribute("productNames", productNames);
		model.addAttribute("orderSearchForm", new OrderSearchForm());
		return "admin_order_management";
	}

	@GetMapping("/admin_order_detail")
	public String showAdminOrderDetail(@RequestParam("orderId") Long orderId, Model model) {
		Order order = adminService.findByOrderId(orderId);
		if (order != null) {
			Map<Long, String> productNames = new HashMap<>();
			Product product = adminService.findByProductId(order.getProductId());
			if (product != null) {
				productNames.put(order.getOrderId(), product.getProductName());
			}

			model.addAttribute("order", order);
			model.addAttribute("productNames", productNames);
		}
		return "admin_order_detail";
	}
	
	@GetMapping("/admin_contact_management")
	public String manageContacts(@ModelAttribute("contactSearchForm") ContactSearchForm contactSearchForm, Model model) {
		List<Contact> contacts = adminService.findContacts(contactSearchForm.getContactId(), contactSearchForm.getStatus());
		model.addAttribute("contactList", contacts);
		model.addAttribute("contactSearchForm", new ContactSearchForm());
		return "admin_contact_management";
	}

	@GetMapping("/admin_contact_detail")
	public String showAdminContactDetail(@RequestParam("contactId") Long contactId, Model model) {
		Contact contact = adminService.findByContactId(contactId);

		if (contact != null) {
			model.addAttribute("contact", contact);
		}
		return "admin_contact_detail";
	}
	
	@GetMapping("/admin_register")
	public String getAdminRegister(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "admin_register";
	}

	@PostMapping("/admin_register")
	public String postRegister(@Validated @ModelAttribute("registerForm") RegisterForm registerForm,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {

			if (bindingResult.hasFieldErrors("userId")) {
				model.addAttribute("userIdError", bindingResult.getFieldError("userId").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("userName")) {
				model.addAttribute("userNameError", bindingResult.getFieldError("userName").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("email")) {
				model.addAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("address")) {
				model.addAttribute("addressError", bindingResult.getFieldError("address").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("telephone")) {
				model.addAttribute("telephoneError", bindingResult.getFieldError("telephone").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("passwordHash")) {
				model.addAttribute("passwordError", bindingResult.getFieldError("passwordHash").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("confirmPassword")) {
				model.addAttribute("confirmPasswordError",
						bindingResult.getFieldError("confirmPassword").getDefaultMessage());
			}

			return "admin_register";
		}

		if (adminService.isUserIdDuplicate(registerForm.getUserId())) {
			model.addAttribute("userIdExistsError", "このユーザーIDはすでに存在します。");
			return "admin_register";
		}

		if (adminService.isTelephoneDuplicate(registerForm.getTelephone())) {
			model.addAttribute("telephoneExistsError", "この電話番号はすでに存在します。");
			return "admin_register";
		}

		if (!registerForm.getPasswordHash().equals(registerForm.getConfirmPassword())) {
			model.addAttribute("passwordUnmatchError", "パスワードと再入力パスワードが一致しません");
			return "admin_register";
		}

		User user = new User();
		user.setUserId(registerForm.getUserId());
		user.setUserName(registerForm.getUserName());
		user.setEmail(registerForm.getEmail());
		user.setAddress(registerForm.getAddress());
		user.setTelephone(registerForm.getTelephone());
		user.setPasswordHash(registerForm.getPasswordHash());
		Role role = roleService.getRoleById(1L);
		user.setRole(role);

		adminService.registerUser(user);

		return "redirect:/admin_register_result";
	}

	@GetMapping("/admin_register_result")
	public String adminRegisterResult() {
			return "admin_register_result";
	}
}
