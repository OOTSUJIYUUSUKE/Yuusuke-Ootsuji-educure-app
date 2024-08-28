package jp.co.example.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jp.co.example.dao.AdminContactDao;
import jp.co.example.dao.AdminDao;
import jp.co.example.dao.AdminOrderDao;
import jp.co.example.dao.AdminProductDao;
import jp.co.example.dao.AdminUserDao;
import jp.co.example.entity.Contact;
import jp.co.example.entity.Order;
import jp.co.example.entity.Product;
import jp.co.example.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminDao adminDao;

    @Mock
    private AdminUserDao adminUserDao;

    @Mock
    private AdminProductDao adminProductDao;

    @Mock
    private AdminOrderDao adminOrderDao;

    @Mock
    private AdminContactDao adminContactDao;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private User user;
    private Product product;
    private Order order;
    private Contact contact;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId("user_123");
        user.setUserName("Test User");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setTelephone("09012345678");
        user.setAddress("Test Address");

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");

        order = new Order();
        order.setOrderId(1L);

        contact = new Contact();
        contact.setContactId(1L);
        contact.setUserName("Test User");
        contact.setStatus("open");
    }

    @Test
    public void authenticate_正常に認証できる場合() {
        doReturn(user).when(adminDao).findByUserId("user_123");
        doReturn(true).when(passwordEncoder).matches("password", "password");

        User result = adminService.authenticate("user_123", "password");

        assertThat(result).isEqualTo(user);
    }

    @Test
    public void findUsers_ユーザーIDまたはメールでユーザーを検索できる場合() {
        doReturn(Arrays.asList(user)).when(adminUserDao).findByUserIdOrEmail("user_123", "test@example.com");

        List<User> result = adminService.findUsers("user_123", "test@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(user);
    }

    @Test
    public void findByUserId_ユーザーIDでユーザーを取得できる場合() {
        doReturn(user).when(adminUserDao).findByUserId("user_123");

        User result = adminService.findByUserId("user_123");

        assertThat(result).isEqualTo(user);
    }

    @Test
    public void updateUser_ユーザー情報を正常に更新できる場合() {
        doReturn(user).when(adminUserDao).findByUserId("user_123");

        adminService.updateUser(user);

        verify(adminUserDao, times(1)).save(user);
    }

    @Test
    public void deleteUserByUserId_ユーザーを正常に削除できる場合() {
        doNothing().when(adminUserDao).deleteByUserId("user_123");

        adminService.deleteUserByUserId("user_123");

        verify(adminUserDao, times(1)).deleteByUserId("user_123");
    }

    @Test
    public void findProducts_商品IDまたはユーザーIDで商品を検索できる場合() {
        doReturn(Arrays.asList(product)).when(adminProductDao).findByProductIdOrUserId(1L, "user_123");

        List<Product> result = adminService.findProducts(1L, "user_123");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(product);
    }

    @Test
    public void findByProductId_商品IDで商品を取得できる場合() {
        doReturn(product).when(adminProductDao).findByProductId(1L);

        Product result = adminService.findByProductId(1L);

        assertThat(result).isEqualTo(product);
    }

    @Test
    public void deleteProductByProductId_商品を正常に削除できる場合() {
        doNothing().when(adminProductDao).deleteByProductId(1L);

        adminService.deleteProductByProductId(1L);

        verify(adminProductDao, times(1)).deleteByProductId(1L);
    }

    @Test
    public void findOrders_注文IDで注文を検索できる場合() {
        doReturn(Arrays.asList(order)).when(adminOrderDao).findByOrderId(1L);

        List<Order> result = adminService.findOrders(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(order);
    }

    @Test
    public void findByOrderId_注文IDで注文を取得できる場合() {
        doReturn(order).when(adminOrderDao).getOrderByOrderId(1L);

        Order result = adminService.findByOrderId(1L);

        assertThat(result).isEqualTo(order);
    }

    @Test
    public void findContacts_コンタクトIDまたはステータスでコンタクトを検索できる場合() {
        doReturn(Arrays.asList(contact)).when(adminContactDao).findByContactIdAndStatus(1L, "open");

        List<Contact> result = adminService.findContacts(1L, "open");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(contact);
    }

    @Test
    public void findByContactId_コンタクトIDでコンタクトを取得できる場合() {
        doReturn(contact).when(adminContactDao).findContactByContactId(1L);

        Contact result = adminService.findByContactId(1L);

        assertThat(result).isEqualTo(contact);
    }

    @Test
    public void isUserIdDuplicate_ユーザーIDが重複している場合() {
        doReturn(true).when(adminDao).existsByUserId("user_123");

        boolean result = adminService.isUserIdDuplicate("user_123");

        assertThat(result).isTrue();
    }

    @Test
    public void isTelephoneDuplicate_電話番号が重複している場合() {
        doReturn(true).when(adminDao).existsByTelephone("09012345678");

        boolean result = adminService.isTelephoneDuplicate("09012345678");

        assertThat(result).isTrue();
    }

    @Test
    public void registerUser_ユーザーを正常に登録できる場合() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        doReturn(encodedPassword).when(passwordEncoder).encode(rawPassword);

        user.setPassword(rawPassword);
        adminService.registerUser(user);

        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        verify(adminDao, times(1)).save(user);
    }
}
