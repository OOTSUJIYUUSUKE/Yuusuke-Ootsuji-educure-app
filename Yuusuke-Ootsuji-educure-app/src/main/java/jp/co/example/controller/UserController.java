package jp.co.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jp.co.example.controller.form.ContactForm;
import jp.co.example.controller.form.LoginForm;
import jp.co.example.controller.form.NewPasswordForm;
import jp.co.example.controller.form.PasswordResetForm;
import jp.co.example.controller.form.RegisterForm;
import jp.co.example.controller.form.SearchForm;
import jp.co.example.controller.form.UserDeleteForm;
import jp.co.example.controller.form.UserEditForm;
import jp.co.example.entity.Contact;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.Role;
import jp.co.example.entity.User;
import jp.co.example.service.ContactService;
import jp.co.example.service.OrderService;
import jp.co.example.service.PasswordResetService;
import jp.co.example.service.ProductService;
import jp.co.example.service.RoleService;
import jp.co.example.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private PasswordResetService passwordResetService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private HttpSession session;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@GetMapping("/top")
	public String getTop() {
		return "top";
	}

	@GetMapping("/user_register")
	public String getUserRegister(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "user_register";
	}

	@PostMapping("/user_register")
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
			if (bindingResult.hasFieldErrors("password")) {
				model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("confirmPassword")) {
				model.addAttribute("confirmPasswordError",
						bindingResult.getFieldError("confirmPassword").getDefaultMessage());
			}
			return "user_register";
		}
		if (userService.isUserIdDuplicate(registerForm.getUserId())) {
			model.addAttribute("userIdExistsError", "このユーザーIDはすでに存在します。");
			return "user_register";
		}
		if (userService.isTelephoneDuplicateForRegister(registerForm.getTelephone())) {
			model.addAttribute("telephoneExistsError", "この電話番号はすでに存在します。");
			return "user_register";
		}
		if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
			model.addAttribute("passwordUnmatchError", "パスワードと再入力パスワードが一致しません");
			return "user_register";
		}
		User user = new User();
		user.setUserId(registerForm.getUserId());
		user.setUserName(registerForm.getUserName());
		user.setEmail(registerForm.getEmail());
		user.setAddress(registerForm.getAddress());
		user.setTelephone(registerForm.getTelephone());
		user.setPassword(registerForm.getPassword());
		Role role = roleService.getRoleById(2);
		user.setRole(role);
		userService.registerUser(user);
		try {
			userService.sendRegistrationEmail(user);
		} catch (MessagingException e) {
			model.addAttribute("emailError", "登録は成功しましたが、確認メールの送信に失敗しました。");
			return "redirect:/user_register_result";
		}
		session.setAttribute("user", user);
		return "redirect:/user_register_result";
	}

	@GetMapping("/user_register_result")
	public String userRegisterResult(Model model) {
		String userId = (String) session.getAttribute("userId");
		if (userId != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				model.addAttribute("loginUserName", user.getUserName());
			}
		}
		return "user_register_result";
	}

	@GetMapping("/user_login")
	public String getUserLogin(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "user_login";
	}

	@PostMapping("/user_login")
	public String postLogin(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors("userId")) {
				model.addAttribute("userIdError", bindingResult.getFieldError("userId").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("password")) {
				model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
			}
			return "user_login";
		}
		User user = userService.authenticate(loginForm.getUserId(), loginForm.getPassword());
		if (user == null) {
			model.addAttribute("errorMessage", "ユーザーIDかパスワードが間違っています");
			return "user_login";
		}
		session.setAttribute("user", user);
		return "redirect:/product_lineup";
	}

	@GetMapping("/password_reset")
	public String showResetForm(Model model) {
		model.addAttribute("passwordResetForm", new PasswordResetForm());
		return "password_reset";
	}

	@PostMapping("/password_reset")
	public String processResetRequest(
			@Validated @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasFieldErrors("email")) {
			model.addAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
			return "password_reset";
		}
		String email = passwordResetForm.getEmail();
		boolean userExists = passwordResetService.checkIfUserExistsByEmail(email);
		if (!userExists) {
			model.addAttribute("errorMessage", "このメールアドレスは登録されていません。");
			return "password_reset";
		}
		String resetToken = passwordResetService.createPasswordResetToken(email);
		passwordResetService.sendPasswordResetEmail(email, resetToken);
		return "redirect:/password_reset_send";
	}

	@GetMapping("/password_reset_send")
	public String showPasswordSendPage(Model model) {
		return "password_reset_send";
	}

	@GetMapping("/set_new_password")
	public String showSetNewPasswordForm(@RequestParam("token") String token, Model model) {
		boolean isValidToken = passwordResetService.validateResetToken(token);
		if (!isValidToken) {
			model.addAttribute("errorMessage", "無効なまたは期限切れのトークンです。");
			return "password_reset_error";
		}
		NewPasswordForm newPasswordForm = new NewPasswordForm();
		model.addAttribute("newPasswordForm", newPasswordForm);
		model.addAttribute("token", token);
		return "set_new_password";
	}

	@PostMapping("/set_new_password")
	public String processNewPassword(@RequestParam("token") String token,
			@Validated @ModelAttribute("newPasswordForm") NewPasswordForm newPasswordForm,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors("newPassword")) {
				model.addAttribute("passwordError",
						bindingResult.getFieldError("newPassword").getDefaultMessage());
			}
			if (bindingResult.hasFieldErrors("confirmPassword")) {
				model.addAttribute("confirmPasswordError",
						bindingResult.getFieldError("confirmPassword").getDefaultMessage());
			}
			model.addAttribute("token", token);
			return "set_new_password";
		}
		if (!newPasswordForm.getNewPassword().equals(newPasswordForm.getConfirmPassword())) {
			model.addAttribute("token", token);
			model.addAttribute("errorMessage", "パスワードと再入力パスワードが一致しません。");
			return "set_new_password";
		}
		if (!passwordResetService.validateResetToken(token)) {
			model.addAttribute("errorMessage", "トークンが無効または期限切れです。");
			return "password_reset";
		}
		passwordResetService.updatePassword(token, newPasswordForm.getNewPassword());
		passwordResetService.invalidateToken(token);
		return "redirect:/password_reset_result";
	}

	@GetMapping("/password_reset_result")
	public String showPasswordResetResult(Model model) {
		return "password_reset_result";
	}

	@GetMapping("/password_reset_error")
	public String showPasswordResetError(Model model) {
		return "password_reset_error";
	}

	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "logout";
	}

	@GetMapping("/access_error")
	public String showAccessError() {
		return "access_error";
	}

	@GetMapping("/mypage")
	public String showMyPage(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("searchForm", new SearchForm());
			return "mypage";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/user_profile")
	public String showUserProfile(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("searchForm", new SearchForm());
			return "user_profile";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/edit_profile")
    public String editUserProfile(Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            UserEditForm userEditForm = new UserEditForm();
            userEditForm.setUserId(user.getUserId());
            userEditForm.setUserName(user.getUserName());
            userEditForm.setEmail(user.getEmail());
            userEditForm.setAddress(user.getAddress());
            userEditForm.setTelephone(user.getTelephone());
            model.addAttribute("userEditForm", userEditForm);
            model.addAttribute("loginUserName", user.getUserName());
            model.addAttribute("searchForm", new SearchForm());       
            return "edit_profile";
        } else {
        	return "redirect:/access_error";
        }
    }
	
	@PostMapping("/edit_profile")
	public String updateProfile(@Validated @ModelAttribute("userEditForm") UserEditForm userEditForm, 
	                            BindingResult bindingResult, 
	                            Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user != null) {
	        if (bindingResult.hasErrors()) {
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
	            model.addAttribute("loginUserName", user.getUserName());
	            model.addAttribute("searchForm", new SearchForm());
	            return "edit_profile";
	        }
	        if (userService.isTelephoneDuplicateForEdit(userEditForm.getTelephone(), user.getUserId())) {
	            model.addAttribute("telephoneExistsError", "この電話番号はすでに使用されています。");
	            model.addAttribute("loginUserName", user.getUserName());
	            model.addAttribute("searchForm", new SearchForm());
	            return "edit_profile";
	        }
	        User updatedUser = new User();
	        updatedUser.setUserId(user.getUserId());
	        updatedUser.setUserName(userEditForm.getUserName());
	        updatedUser.setEmail(userEditForm.getEmail());
	        updatedUser.setAddress(userEditForm.getAddress());
	        updatedUser.setTelephone(userEditForm.getTelephone());
	        userService.updateUser(updatedUser);
	        session.setAttribute("user", updatedUser);
	        model.addAttribute("loginUserName", updatedUser.getUserName());
	        model.addAttribute("searchForm", new SearchForm());
	        return "redirect:/edit_profile_result";
	    } else {
	        return "redirect:/access_error";
	    }
	}
	
	@GetMapping("/edit_profile_result")
    public String editUserProfileResult(Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("loginUserName", user.getUserName());
            model.addAttribute("searchForm", new SearchForm());
            return "edit_profile_result";
        } else {
        	return "redirect:/access_error";
        }
    }

	@GetMapping("/buy_history")
	public String showPurchaseHistory(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			String userId = user.getUserId();
			List<Order> orders = orderService.findOrdersByUserIdOrderByCreatedAtDesc(userId);
			List<Product> products = new ArrayList<>();
			for (Order order : orders) {
				Product product = productService.findProductById(order.getProductId());
				products.add(product);
			}
			model.addAttribute("searchForm", new SearchForm());
			model.addAttribute("products", products);
			return "buy_history";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/sell_history")
	public String showSellHistory(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			String userId = user.getUserId();
			List<Product> products = productService.findProductsByUserIdOrderByCreatedAtDesc(userId);
			model.addAttribute("searchForm", new SearchForm());
			model.addAttribute("products", products);
			return "sell_history";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/user_delete")
	public String showUserDeleteForm(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("searchForm", new SearchForm());
			model.addAttribute("userDeleteForm", new UserDeleteForm());
			return "user_delete";
		} else {
			return "redirect:/access_error";
		}
	}

	@PostMapping("/user_delete")
	public String deleteUser(@Validated @ModelAttribute("userDeleteForm") UserDeleteForm userDeleteForm,
	                         BindingResult bindingResult,
	                         Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/access_error";
	    }

	    if (bindingResult.hasErrors()) {
	        return "user_delete";
	    }

	    boolean isPasswordMatch = passwordEncoder.matches(userDeleteForm.getPassword(), user.getPassword());
	    if (!isPasswordMatch) {
	        model.addAttribute("error", "パスワードが間違っています。もう一度お試しください。");
	        return "user_delete";
	    }

	    boolean isDeleted = userService.deleteUser(user.getUserId());
	    if (!isDeleted) {
	        model.addAttribute("error", "ユーザー削除に失敗しました。");
	        return "user_delete";
	    }

	    session.invalidate();
	    return "redirect:/user_delete_result";
	}


	@GetMapping("/user_delete_result")
	public String showUserDeleteResult() {
		return "user_delete_result";
	}

	@GetMapping("/contact")
	public String showContactForm(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("searchForm", new SearchForm());
			model.addAttribute("contactForm", new ContactForm());
			return "contact";
		} else {
			return "redirect:/access_error";
		}
	}

	@PostMapping("/contact")
	public String submitContactForm(@Validated @ModelAttribute("contactForm") ContactForm form, 
            BindingResult bindingResult, 
            Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
	        if (bindingResult.hasErrors()) {
	            if (bindingResult.hasFieldErrors("userName")) {
	                model.addAttribute("userNameError", bindingResult.getFieldError("userName").getDefaultMessage());
	            }
	            if (bindingResult.hasFieldErrors("email")) {
	                model.addAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
	            }
	            if (bindingResult.hasFieldErrors("subject")) {
	                model.addAttribute("subjectError", bindingResult.getFieldError("subject").getDefaultMessage());
	            }
	            if (bindingResult.hasFieldErrors("message")) {
	                model.addAttribute("messageError", bindingResult.getFieldError("message").getDefaultMessage());
	            }
	            model.addAttribute("loginUserName", user.getUserName());
	            model.addAttribute("searchForm", new SearchForm());
	            return "contact";
	        }
	        Contact contact = new Contact();
	        contact.setUserName(form.getUserName());
	        contact.setEmail(form.getEmail());
	        contact.setSubject(form.getSubject());
	        contact.setMessage(form.getMessage());
	        contactService.saveContact(contact);
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(form.getEmail());
	        message.setSubject("お問い合わせありがとうございます");
	        message.setText("お名前: " + form.getUserName() + "\n"
	                + "件名: " + form.getSubject() + "\n"
	                + "お問い合わせ内容: " + form.getMessage() + "\n\n"
	                + "お問い合わせを受け付けました。追ってご連絡いたします。");
	        mailSender.send(message);
	        return "redirect:/contact_send";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/contact_send")
	public String submitContactResult(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("searchForm", new SearchForm());
			return "contact_send";
		} else {
			return "redirect:/access_error";
		}
	}
}
