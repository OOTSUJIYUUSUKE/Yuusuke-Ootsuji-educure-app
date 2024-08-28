package jp.co.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jp.co.example.dao.UserDao;
import jp.co.example.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    
    private User user1;
    private User user2;
    private User user3;
    private String password;
    private String encodedPassword;
    
    @BeforeEach
    public void setUp() {
        password = "password";
        encodedPassword = "encodedPassword";

        user1 = new User();
        user1.setUserId("user_123");
        user1.setPassword(encodedPassword);
        user1.setUserName("Test User 1");
        user1.setEmail("user1@example.com");
        user1.setTelephone("09012345678");
        user1.setAddress("Tokyo");

        user2 = new User();
        user2.setUserId("user_456");
        user2.setPassword(encodedPassword);
        user2.setUserName("Test User 2");
        user2.setEmail("user2@example.com");
        user2.setTelephone("08012345678");
        user2.setAddress("Osaka");

        user3 = new User();
        user3.setUserId("user_789");
        user3.setPassword(encodedPassword);
        user3.setUserName("Test User 3");
        user3.setEmail("user3@example.com");
        user3.setTelephone("07012345678");
        user3.setAddress("Nagoya");
    }


    @Test
    public void authenticate_正常に認証できる場合() {
        doReturn(user1).when(userDao).findByUserId("user_123");
        doReturn(true).when(passwordEncoder).matches(password, encodedPassword);

        User result = userService.authenticate("user_123", password);

        assertThat(result).isEqualTo(user1);
    }

    @Test
    public void authenticate_ユーザーが存在しない場合() {
        doReturn(null).when(userDao).findByUserId("user_123");

        User result = userService.authenticate("user_123", password);

        assertThat(result).isNull();
    }

    @Test
    public void authenticate_パスワードが不正の場合() {
        doReturn(user1).when(userDao).findByUserId("user_123");
        doReturn(false).when(passwordEncoder).matches(password, encodedPassword);

        User result = userService.authenticate("user_123", password);

        assertThat(result).isNull();
    }

    @Test
    public void isUserIdDuplicate_ユーザーIDが重複している場合() {
        doReturn(true).when(userDao).existsByUserId("user_123");

        boolean result = userService.isUserIdDuplicate("user_123");

        assertThat(result).isTrue();
    }

    @Test
    public void isUserIdDuplicate_ユーザーIDが重複していない場合() {
        doReturn(false).when(userDao).existsByUserId("user_123");

        boolean result = userService.isUserIdDuplicate("user_123");

        assertThat(result).isFalse();
    }

    @Test
    public void isTelephoneDuplicateForRegister_電話番号が重複している場合() {
        doReturn(true).when(userDao).existsByTelephone("09012345678");

        boolean result = userService.isTelephoneDuplicateForRegister("09012345678");

        assertThat(result).isTrue();
    }

    @Test
    public void isTelephoneDuplicateForRegister_電話番号が重複していない場合() {
        doReturn(false).when(userDao).existsByTelephone("09012345678");

        boolean result = userService.isTelephoneDuplicateForRegister("09012345678");

        assertThat(result).isFalse();
    }

    @Test
    public void registerUser_正常にユーザーを登録できる() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setPassword(rawPassword);

        doReturn(encodedPassword).when(passwordEncoder).encode(rawPassword);

        userService.registerUser(user);

        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        verify(userDao, times(1)).save(user);
    }

    @Test
    public void sendRegistrationEmail_正常にメールを送信できる場合() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        doReturn(mimeMessage).when(mailSender).createMimeMessage();
        doNothing().when(mailSender).send(mimeMessage);

        userService.sendRegistrationEmail(user1);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void getUserById_ユーザーIDでユーザーを取得できる() {
        doReturn(user1).when(userDao).findByUserId("user_123");

        User result = userService.getUserById("user_123");

        assertThat(result).isEqualTo(user1);
    }

    @Test
    public void isTelephoneDuplicateForEdit_電話番号が他のユーザーに使用されている場合() {
        doReturn(Optional.of(user2)).when(userDao).findByTelephone("09012345678");

        boolean result = userService.isTelephoneDuplicateForEdit("09012345678", "user_123");

        assertThat(result).isTrue();
    }

    @Test
    public void isTelephoneDuplicateForEdit_電話番号が他のユーザーに使用されていない場合() {
        doReturn(Optional.of(user1)).when(userDao).findByTelephone("09012345678");

        boolean result = userService.isTelephoneDuplicateForEdit("09012345678", "user_123");

        assertThat(result).isFalse();
    }

    @Test
    public void updateUser_ユーザー情報を正常に更新できる場合() {
        User updatedUser = new User();
        updatedUser.setUserId("user_123");
        updatedUser.setUserName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setAddress("Updated Address");
        updatedUser.setTelephone("09087654321");

        doReturn(user1).when(userDao).findByUserId("user_123");

        userService.updateUser(updatedUser);

        assertThat(user1.getUserName()).isEqualTo("Updated Name");
        assertThat(user1.getEmail()).isEqualTo("updated@example.com");
        assertThat(user1.getAddress()).isEqualTo("Updated Address");
        assertThat(user1.getTelephone()).isEqualTo("09087654321");

        verify(userDao, times(1)).save(user1);
    }

    @Test
    public void deleteUser_ユーザーを正常に削除できる場合() {
        doReturn(user1).when(userDao).findByUserId("user_123");

        boolean result = userService.deleteUser("user_123");

        assertThat(result).isTrue();
        verify(userDao, times(1)).delete(user1);
    }

    @Test
    public void deleteUser_ユーザーが存在しない場合() {
        doReturn(null).when(userDao).findByUserId("user_123");

        boolean result = userService.deleteUser("user_123");

        assertThat(result).isFalse();
        verify(userDao, times(0)).delete(any(User.class));
    }
}