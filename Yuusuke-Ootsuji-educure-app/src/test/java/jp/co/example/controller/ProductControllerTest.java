package jp.co.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.stripe.exception.ApiException;

import jp.co.example.controller.form.OrderRequest;
import jp.co.example.controller.form.ProductForm;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;
import jp.co.example.service.OrderService;
import jp.co.example.service.ProductService;
import jp.co.example.service.StripeService;

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private StripeService stripeService;
    
    @Test
    void testShowProductLineup_UserLoggedIn() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("testuser");
        mockUser.setUserName("Test User");

        List<Product> products = List.of(new Product(), new Product());
        doReturn(products).when(productService).findTop10ByOrderByCreatedAtDesc();

        mockMvc.perform(get("/product_lineup").sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("product_lineup"))
                .andExpect(model().attributeExists("loginUserName", "products", "searchForm"))
                .andExpect(model().attribute("products", products));
    }
    
    @Test
    void testShowProductLineup_UserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/product_lineup"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    void testSearchResult_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");
        mockUser.setUserName("Test User");

        List<Product> products = List.of(new Product(), new Product());
        
        doReturn(products).when(productService).getProductsByName("TestProduct");

        mockMvc.perform(get("/search_result")
                .sessionAttr("user", mockUser)
                .param("search", "TestProduct"))
                .andExpect(status().isOk())
                .andExpect(view().name("search_result"))
                .andExpect(model().attributeExists("products", "searchQuery"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("searchQuery", "TestProduct"));
    }

    @Test
    void testSearchResult_UserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/search_result")
                .param("search", "TestProduct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }


    @Test
    void testShowProductDetail_UserLoggedIn() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("testuser");
        mockUser.setUserName("Test User");

        Product mockProduct = new Product();
        doReturn(mockProduct).when(productService).findProductById(anyLong());

        mockMvc.perform(get("/product_detail").param("product_id", "1").sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("product_detail"))
                .andExpect(model().attributeExists("loginUserName", "product", "searchForm"))
                .andExpect(model().attribute("product", mockProduct));
    }
    
    @Test
    void testShowProductDetail_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");
        mockUser.setUserName("Test User");

        Product product = new Product();
        
        doReturn(product).when(productService).findProductById(1L);

        mockMvc.perform(get("/product_detail")
                .sessionAttr("user", mockUser)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product_detail"))
                .andExpect(model().attributeExists("product", "searchForm"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    void testShowProductDetail_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/product_detail")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    void testShowBuyProductDetail_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");
        mockUser.setUserName("Test User");

        Product product = new Product();
        
        doReturn(product).when(productService).findProductById(1L);

        mockMvc.perform(get("/buy_product_detail")
                .sessionAttr("user", mockUser)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("buy_product_detail"))
                .andExpect(model().attributeExists("product", "searchForm"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    void testShowBuyProductDetail_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/buy_product_detail")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }

    @Test
    void testShowSellProductDetail_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");
        mockUser.setUserName("Test User");

        Product product = new Product();
        
        doReturn(product).when(productService).findProductById(1L);

        mockMvc.perform(get("/sell_product_detail")
                .sessionAttr("user", mockUser)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sell_product_detail"))
                .andExpect(model().attributeExists("product", "searchForm"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    void testShowSellProductDetail_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/sell_product_detail")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    void testShowPurchasePage_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");
        mockUser.setUserName("Test User");
        mockUser.setAddress("Test Address");

        Product product = new Product();
        
        doReturn(product).when(productService).findProductById(1L);

        mockMvc.perform(get("/product_purchase")
                .sessionAttr("user", mockUser)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product_purchase"))
                .andExpect(model().attributeExists("loginUserName", "registeredAddress", "product"))
                .andExpect(model().attribute("loginUserName", "Test User"))
                .andExpect(model().attribute("registeredAddress", "Test Address"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    void testShowPurchasePage_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/product_purchase")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("user1");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setPrice(BigDecimal.valueOf(1000));
        orderRequest.setPaymentMethod("stripe");
        orderRequest.setShippingAddress("Test Address");

        Product product = new Product();
        product.setProductName("Test Product");

        doReturn("Test Product").when(productService).getProductNameById(1L);
        doReturn("session_123").when(stripeService).createCheckoutSession(any(Order.class), anyString());

        mockMvc.perform(post("/create-checkout-session")
                .sessionAttr("user", mockUser)
                .contentType("application/json")
                .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"sessionId\": \"session_123\"}"));
    }

    @Test
    void testCreateOrder_UserNoLoggedIn() throws Exception {
        mockMvc.perform(post("/create-checkout-session")
                .contentType("application/json")
                .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not found in session"));
    }

	@Test
	void testCreateOrder_StripeException() throws Exception {
		User mockUser = new User();
		mockUser.setUserId("user1");

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setProductId(1L);
		orderRequest.setPrice(BigDecimal.valueOf(1000));
		orderRequest.setPaymentMethod("stripe");
		orderRequest.setShippingAddress("Test Address");

		doReturn("Test Product").when(productService).getProductNameById(1L);
		doThrow(new ApiException("Stripe Error", "request-id", "code", 500, null))
				.when(stripeService).createCheckoutSession(any(Order.class), anyString());

		String responseContent = mockMvc.perform(post("/create-checkout-session")
	            .sessionAttr("user", mockUser)
	            .contentType("application/json")
	            .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
	            .andExpect(status().isInternalServerError())
	            .andReturn()
	            .getResponse()
	            .getContentAsString();
	    assertTrue(responseContent.contains("Session creation failed: Stripe Error"));
	}

	@Test
	void testShowSellPage_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/sell")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell"))
	            .andExpect(model().attributeExists("loginUserName", "productForm"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testShowSellPage_UserNoLoggedIn() throws Exception {
	    mockMvc.perform(get("/sell"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}

	@Test
	void testSubmitProduct_Success() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("user1");
	    mockUser.setUserName("Test User");

	    doNothing().when(productService).saveProduct(any(ProductForm.class));

	    byte[] fileContent = "test content".getBytes();
	    
	    mockMvc.perform(multipart("/sell")
	            .file(new MockMultipartFile("imageData", "test-image.jpg", "image/jpeg", fileContent))
	            .param("productName", "Test Product")
	            .param("description", "Test Description")
	            .param("price", "1000")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/sell_submit"))
	            .andExpect(request().sessionAttribute("user", mockUser));
	}

	@Test
	void testSubmitProduct_BindingErrors() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("user1");
	    mockUser.setUserName("Test User");

	    mockMvc.perform(multipart("/sell")
	            .param("productName", "")
	            .param("description", "Test Description")
	            .param("price", "1000")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell"))
	            .andExpect(model().attributeExists("productNameError"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testSubmitProduct_FileSizeExceeded() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("user1");
	    mockUser.setUserName("Test User");

	    byte[] largeFile = new byte[10 * 1024 * 1024 + 1];

	    mockMvc.perform(multipart("/sell")
	            .file(new MockMultipartFile("imageData", "test-image.jpg", "image/jpeg", largeFile))
	            .param("productName", "Test Product")
	            .param("description", "Test Description")
	            .param("price", "1000")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell"))
	            .andExpect(model().attributeExists("imageSizeError"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testSubmitProduct_IOException() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserId("user1");
	    mockUser.setUserName("Test User");

	    doThrow(new IOException("Test Exception")).when(productService).saveProduct(any(ProductForm.class));

	    mockMvc.perform(multipart("/sell")
	            .file(new MockMultipartFile("imageData", "test-image.jpg", "image/jpeg", "content".getBytes()))
	            .param("productName", "Test Product")
	            .param("description", "Test Description")
	            .param("price", "1000")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell"))
	            .andExpect(model().attributeExists("error"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}
	
	@Test
	void testSubmitResult_UserLoggedIn() throws Exception {
	    User mockUser = new User();
	    mockUser.setUserName("Test User");

	    mockMvc.perform(get("/sell_submit")
	            .sessionAttr("user", mockUser))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell_submit"))
	            .andExpect(model().attribute("loginUserName", "Test User"));
	}

	@Test
	void testSubmitResult_UserNoLoggedIn() throws Exception {
	    mockMvc.perform(get("/sell_submit"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
}