package jp.co.example.service;

import jp.co.example.dao.PasswordResetDao;
import jp.co.example.dao.UserDao;
import jp.co.example.entity.PasswordReset;
import jp.co.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordResetDao passwordResetDao;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User user;
    private PasswordReset passwordReset;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId("user_123");
        user.setEmail("test@example.com");

        passwordReset = new PasswordReset();
        passwordReset.setResetToken(UUID.randomUUID().toString());
        passwordReset.setUserId(user.getUserId());
        passwordReset.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void checkIfUserExistsByEmail_ユーザーが存在する場合() {
        doReturn(true).when(userDao).existsByEmail(user.getEmail());

        boolean result = passwordResetService.checkIfUserExistsByEmail(user.getEmail());

        assertThat(result).isTrue();
        verify(userDao, times(1)).existsByEmail(user.getEmail());
    }

    @Test
    public void checkIfUserExistsByEmail_ユーザーが存在しない場合() {
        doReturn(false).when(userDao).existsByEmail("nonexistent@example.com");

        boolean result = passwordResetService.checkIfUserExistsByEmail("nonexistent@example.com");

        assertThat(result).isFalse();
        verify(userDao, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    public void createPasswordResetToken_ユーザーが存在する場合() {
        doReturn(Optional.of(user)).when(userDao).findByEmail(user.getEmail());

        String token = passwordResetService.createPasswordResetToken(user.getEmail());

        assertThat(token).isNotNull();
        verify(passwordResetDao, times(1)).save(any(PasswordReset.class));
    }

    @Test
    public void createPasswordResetToken_ユーザーが存在しない場合に例外をスローする() {
        doReturn(Optional.empty()).when(userDao).findByEmail("nonexistent@example.com");

        assertThrows(RuntimeException.class, () -> passwordResetService.createPasswordResetToken("nonexistent@example.com"));

        verify(passwordResetDao, times(0)).save(any(PasswordReset.class));
    }

    @Test
    public void sendPasswordResetEmail_正常にメールを送信できる() {
        String token = UUID.randomUUID().toString();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        passwordResetService.sendPasswordResetEmail(user.getEmail(), token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void validateResetToken_トークンが有効な場合() {
        doReturn(Optional.of(passwordReset)).when(passwordResetDao).findByResetToken(passwordReset.getResetToken());

        boolean result = passwordResetService.validateResetToken(passwordReset.getResetToken());

        assertThat(result).isTrue();
        verify(passwordResetDao, times(1)).findByResetToken(passwordReset.getResetToken());
    }

    @Test
    public void validateResetToken_トークンが無効な場合() {
        doReturn(Optional.empty()).when(passwordResetDao).findByResetToken("invalid_token");

        boolean result = passwordResetService.validateResetToken("invalid_token");

        assertThat(result).isFalse();
        verify(passwordResetDao, times(1)).findByResetToken("invalid_token");
    }

    @Test
    public void invalidateToken_正常にトークンを無効にできる() {
        doNothing().when(passwordResetDao).deleteByResetToken(passwordReset.getResetToken());

        passwordResetService.invalidateToken(passwordReset.getResetToken());

        verify(passwordResetDao, times(1)).deleteByResetToken(passwordReset.getResetToken());
    }

    @Test
    public void getUserByResetToken_トークンに対応するユーザーが存在する場合() {
        doReturn(Optional.of(passwordReset)).when(passwordResetDao).findByResetToken(passwordReset.getResetToken());
        doReturn(user).when(userDao).findByUserId(user.getUserId());

        Optional<User> result = passwordResetService.getUserByResetToken(passwordReset.getResetToken());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void getUserByResetToken_トークンに対応するユーザーが存在しない場合() {
        doReturn(Optional.empty()).when(passwordResetDao).findByResetToken("invalid_token");

        Optional<User> result = passwordResetService.getUserByResetToken("invalid_token");

        assertThat(result).isNotPresent();
    }

    @Test
    public void updatePassword_正常にパスワードを更新できる() {
        String newPassword = "newPassword";

        doReturn(Optional.of(passwordReset)).when(passwordResetDao).findByResetToken(passwordReset.getResetToken());
        doReturn(user).when(userDao).findByUserId(user.getUserId());

        passwordResetService.updatePassword(passwordReset.getResetToken(), newPassword);

        verify(userDao, times(1)).save(user);
        verify(passwordResetDao, times(1)).deleteByResetToken(passwordReset.getResetToken());
        assertThat(passwordEncoder.matches(newPassword, user.getPassword())).isTrue();
    }

    @Test
    public void updatePassword_トークンが無効な場合に例外をスローする() {
        doReturn(Optional.empty()).when(passwordResetDao).findByResetToken("invalid_token");

        assertThrows(RuntimeException.class, () -> passwordResetService.updatePassword("invalid_token", "newPassword"));

        verify(userDao, times(0)).save(any(User.class));
        verify(passwordResetDao, times(0)).deleteByResetToken("invalid_token");
    }
}
