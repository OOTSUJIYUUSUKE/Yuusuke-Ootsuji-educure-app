package integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.stripe.exception.ApiException;

import jakarta.transaction.Transactional;
import jp.co.example.YuusukeOotsujiEducureAppApplication;
import jp.co.example.controller.form.OrderRequest;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;
import jp.co.example.service.ProductService;
import jp.co.example.service.StripeService;
import jp.co.example.service.UserService;

@SpringBootTest(classes = YuusukeOotsujiEducureAppApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@MockBean
	private StripeService stripeService;
	
	@Test
	@DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testShowProductLineup_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

        List<Product> products = productService.findTop10ByOrderByCreatedAtDesc();

        mockMvc.perform(get("/product_lineup").sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("product_lineup"))
                .andExpect(model().attributeExists("loginUserName", "products", "searchForm"))
                .andExpect(model().attribute("products", products));
    }
    
    @Test
    @Transactional
    void testShowProductLineup_UserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/product_lineup"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testSearchResult_UserLoggedIn() throws Exception {
    	User user = userService.getUserById("tanaka_123");

    	MvcResult result = mockMvc.perform(get("/search_result")
                .sessionAttr("user", user)
                .param("search", "トマト"))
                .andExpect(status().isOk())
                .andExpect(view().name("search_result"))
                .andExpect(model().attributeExists("products", "searchQuery"))
                .andExpect(model().attribute("searchQuery", "トマト"))
                .andReturn();

        List<?> products = (List<?>) result.getModelAndView().getModel().get("products");
        
        assertNotNull(products);
        assertEquals(1, products.size());
        
        Product product = (Product) products.get(0);
        assertEquals("トマト", product.getProductName());
    }

    @Test
    void testSearchResult_UserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/search_result")
                .param("search", "TestProduct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testShowProductDetail_UserLoggedIn() throws Exception {
    	User user = userService.getUserById("tanaka_123");

        Product product = productService.findProductById(1L);

        mockMvc.perform(get("/product_detail").param("product_id", "1").sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("product_detail"))
                .andExpect(model().attributeExists("loginUserName", "product", "searchForm"))
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
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testShowBuyProductDetail_UserLoggedIn() throws Exception {
    	User user = userService.getUserById("tanaka_123");

    	Product product = productService.findProductById(1L);

        mockMvc.perform(get("/buy_product_detail")
                .sessionAttr("user", user)
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
    @DataSet({"datasets/role.yml", "datasets/orders.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testShowSellProductDetail_UserLoggedIn() throws Exception {
        User user = userService.getUserById("tanaka_123");

        mockMvc.perform(get("/sell_product_detail")
                .sessionAttr("user", user)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sell_product_detail"))
                .andExpect(model().attributeExists("product", "searchForm"))
                .andExpect(model().attribute("product", productService.findProductById(1L)))
                .andExpect(model().attribute("loginUserName", user.getUserName()));
    }


    @Test
    void testShowSellProductDetail_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/sell_product_detail")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }
    
    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testShowPurchasePage_UserLoggedIn() throws Exception {
        User user = userService.getUserById("tanaka_123");

        MvcResult result = mockMvc.perform(get("/product_purchase")
                .sessionAttr("user", user)
                .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product_purchase"))
                .andExpect(model().attributeExists("product", "loginUserName", "registeredAddress"))
                .andReturn();

        Product product = (Product) result.getModelAndView().getModel().get("product");
        assertEquals("トマト", product.getProductName());
        assertEquals("田中太郎", result.getModelAndView().getModel().get("loginUserName"));
        assertEquals("大阪府大阪市1-1-1", result.getModelAndView().getModel().get("registeredAddress"));
    }


    @Test
    @Transactional
    void testShowPurchasePage_UserNoLoggedIn() throws Exception {
        mockMvc.perform(get("/product_purchase")
                .param("product_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access_error"));
    }

    @Test
    @DataSet({"datasets/role.yml","datasets/users.yml", "datasets/products.yml", "datasets/orders.yml"})
    @Transactional
    void testCreateOrder_UserLoggedIn() throws Exception {
    	User user = userService.getUserById("tanaka_123");
    	
    	doReturn("session_123").when(stripeService).createCheckoutSession(any(Order.class), any(String.class));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setPrice(BigDecimal.valueOf(1000));
        orderRequest.setPaymentMethod("stripe");
        orderRequest.setShippingAddress("大阪府大阪市1-1-1");

        mockMvc.perform(post("/create-checkout-session")
                .sessionAttr("user", user)
                .contentType("application/json")
                .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"sessionId\": \"session_123\"}"));
    }

    @Test
    @Transactional
    void testCreateOrder_UserNoLoggedIn() throws Exception {
        mockMvc.perform(post("/create-checkout-session")
                .contentType("application/json")
                .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not found in session"));
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    void testCreateOrder_StripeException() throws Exception {
        User user = userService.getUserById("tanaka_123");

        doThrow(new ApiException("Stripe Error", "request-id", "error-code", 500, null))
        .when(stripeService).createCheckoutSession(any(Order.class), any(String.class));

        String responseContent = mockMvc.perform(post("/create-checkout-session")
                .sessionAttr("user", user)
                .contentType("application/json")
                .content("{\"productId\": 1, \"price\": 1000, \"paymentMethod\": \"stripe\", \"shippingAddress\": \"Test Address\"}"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(responseContent.contains("Session creation failed: Stripe Error"));
    }
    
	@Test
	@DataSet({"datasets/role.yml","datasets/users.yml", "datasets/products.yml"})
	@Transactional
	void testShowSellPage_UserLoggedIn() throws Exception {
		User user = userService.getUserById("tanaka_123");

	    mockMvc.perform(get("/sell")
	            .sessionAttr("user", user))
	            .andExpect(status().isOk())
	            .andExpect(view().name("sell"))
	            .andExpect(model().attributeExists("loginUserName", "productForm"))
	            .andExpect(model().attribute("loginUserName", "田中太郎"));
	}

	@Test
	@Transactional
	void testShowSellPage_UserNoLoggedIn() throws Exception {
	    mockMvc.perform(get("/sell"))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/access_error"));
	}
}
