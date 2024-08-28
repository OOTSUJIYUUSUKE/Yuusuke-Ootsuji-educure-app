package jp.co.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpSession;
import jp.co.example.controller.form.OrderRequest;
import jp.co.example.controller.form.ProductForm;
import jp.co.example.controller.form.SearchForm;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;
import jp.co.example.service.OrderService;
import jp.co.example.service.ProductService;
import jp.co.example.service.StripeService;

@Controller
public class ProductController {

	@Autowired
	private HttpSession session;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StripeService stripeService;

	@GetMapping("/product_lineup")
	public String showProductLineup(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			List<Product> products = productService.findTop10ByOrderByCreatedAtDesc();
			for (Product product : products) {
				System.out.println("Product ID: " + product.getProductId());
				System.out.println("Is Sold Out: " + product.isSoldOut());
			}
			model.addAttribute("products", products);
			model.addAttribute("searchForm", new SearchForm());
			return "product_lineup";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/search_result")
	public String searchResult(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			String productName = searchForm.getSearch();
			List<Product> products = productService.getProductsByName(productName);
			model.addAttribute("products", products);
			model.addAttribute("searchQuery", productName);
			return "search_result";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/product_detail")
	public String showProductDetail(@RequestParam("product_id") Long productId, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			Product product = productService.findProductById(productId);
			model.addAttribute("product", product);
			model.addAttribute("searchForm", new SearchForm());
			return "product_detail";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/buy_product_detail")
	public String showBuyProductDetail(@RequestParam("product_id") Long productId, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			Product product = productService.findProductById(productId);
			model.addAttribute("product", product);
			model.addAttribute("searchForm", new SearchForm());
			return "buy_product_detail";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/sell_product_detail")
	public String showSellProductDetail(@RequestParam("product_id") Long productId, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			Product product = productService.findProductById(productId);
			model.addAttribute("product", product);
			model.addAttribute("searchForm", new SearchForm());
			return "sell_product_detail";
		} else {
			return "redirect:/access_error";
		}
	}

	@GetMapping("/product_purchase")
	public String showPurchasePage(@RequestParam("product_id") Long productId, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("registeredAddress", user.getAddress());
			Product product = productService.findProductById(productId);
			model.addAttribute("product", product);
			return "product_purchase";
		} else {
			return "redirect:/access_error";
		}
	}

	@PostMapping("/create-checkout-session")
	@ResponseBody
	public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found in session");
		}
		String userId = user.getUserId();
		Long productId = orderRequest.getProductId();
		BigDecimal price = orderRequest.getPrice();
		String paymentMethod = orderRequest.getPaymentMethod();
		String shippingAddress = orderRequest.getShippingAddress();
		String productName = productService.getProductNameById(productId);
		// 注文の作成
		Order order = new Order();
		order.setUserId(userId);
		order.setProductId(productId);
		order.setPrice(price);
		order.setPaymentMethod(paymentMethod);
		order.setShippingAddress(shippingAddress);
		order.setCreatedAt(LocalDateTime.now());
		order.setStatus("waiting"); // 初期ステータスを "waiting" に設定
		try {
			// Stripeの決済セッションを作成
			String sessionId = stripeService.createCheckoutSession(order, productName);
			order.setSessionId(sessionId); // セッションIDを設定
		} catch (StripeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Session creation failed: " + e.getMessage() + "\"}");
		}
		// 注文を保存（statusは"waiting"のまま）
		orderService.saveOrder(order);
		return ResponseEntity.ok("{\"sessionId\": \"" + order.getSessionId() + "\"}");
	}

	@GetMapping("/product_purchase_success")
	public String productPurchaseSuccess(@RequestParam("session_id") String sessionId, Model model) {
		Order order = orderService.findOrderBySessionId(sessionId);
		if (order != null) {
			// ステータスを "success" に更新
			order.setStatus("success");
			orderService.saveOrder(order); // ステータスの変更を保存	        
			Product product = productService.findProductById(order.getProductId());
			if (product != null) {
				product.setSoldOut(true);
				productService.updateStatus(product);
			}
			model.addAttribute("sessionId", sessionId);
			model.addAttribute("status", order.getStatus()); // ステータスを追加
		}
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
		}
		String productName = productService.getProductNameById(order.getProductId());
		model.addAttribute("productName", productName);
		model.addAttribute("shippingAddress", order.getShippingAddress());
		model.addAttribute("totalAmount", order.getPrice().add(BigDecimal.valueOf(300)));
		return "product_purchase_success";
	}

	@GetMapping("/product_purchase_error")
	public String productPurchaseError(@RequestParam("session_id") String sessionId, Model model) {
		if (sessionId != null) {
			// セッションIDに基づいて注文を検索
			Order order = orderService.findOrderBySessionId(sessionId);
			if (order != null) {
				// ステータスを "failed" に更新
				order.setStatus("failed");
				orderService.saveOrder(order); // ステータスの変更を保存
			}
		}
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
		}
		return "product_purchase_error";
	}

	@GetMapping("/sell")
	public String showSellPage(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			model.addAttribute("productForm", new ProductForm());
			return "sell";
		} else {
			return "redirect:/access_error";
		}
	}

	@PostMapping("/sell")
	public String submitProduct(@Validated @ModelAttribute("productForm") ProductForm productForm,
			BindingResult bindingResult,
			Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			if (bindingResult.hasErrors()) {
				if (bindingResult.hasFieldErrors("imageData")) {
					model.addAttribute("imageError", bindingResult.getFieldError("imageData").getDefaultMessage());
				}
				if (bindingResult.hasFieldErrors("productName")) {
					model.addAttribute("productNameError",
							bindingResult.getFieldError("productName").getDefaultMessage());
				}
				if (bindingResult.hasFieldErrors("description")) {
					model.addAttribute("descriptionError",
							bindingResult.getFieldError("description").getDefaultMessage());
				}
				if (bindingResult.hasFieldErrors("price")) {
					model.addAttribute("priceError", bindingResult.getFieldError("price").getDefaultMessage());
				}
				model.addAttribute("loginUserName", user.getUserName());
				return "sell";
			}
			long maxAllowedSize = 10 * 1024 * 1024; // 10MB
			for (MultipartFile file : productForm.getImageData()) {
				if (file.getSize() > maxAllowedSize) {
					model.addAttribute("imageSizeError", "ファイルサイズが10MBを超えています。");
					model.addAttribute("loginUserName", user.getUserName());
					return "sell";
				}
			}
			try {
				productForm.setUserId(user.getUserId());
				productService.saveProduct(productForm);
				model.addAttribute("loginUserName", user.getUserName());
				return "redirect:/sell_submit";

			} catch (IOException e) {
				model.addAttribute("error", "商品登録中にエラーが発生しました。");
				model.addAttribute("loginUserName", user.getUserName());
				e.printStackTrace();
				return "sell";
			}
		} else {
			return "redirect:/access_error";
		}
	}
	
	@GetMapping("/sell_submit")
	public String submitResult(Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("loginUserName", user.getUserName());
			return "sell_submit";
		} else {
			return "redirect:/access_error";
		}
	}
}