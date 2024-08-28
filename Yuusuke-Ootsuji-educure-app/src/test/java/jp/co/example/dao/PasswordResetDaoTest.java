package jp.co.example.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jp.co.example.entity.PasswordReset;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PasswordResetDaoTest {

    @Autowired
    private PasswordResetDao passwordResetDao;

    @Test
    @DataSet("datasets/password_reset.yml")
    @Transactional
    public void findByResetToken_正常にリセットトークンで検索できること() {
        Optional<PasswordReset> passwordReset = passwordResetDao.findByResetToken("valid-token");
        assertThat(passwordReset).isPresent();
        assertThat(passwordReset.get().getUserId()).isEqualTo("tanaka_123");
        assertThat(passwordReset.get().getExpiresAt()).isEqualTo("2025-09-01T00:00:00");
    }

    @Test
    @DataSet("datasets/password_reset.yml")
    @Transactional
    public void findByResetToken_存在しないリセットトークンで検索した場合に結果が空であること() {
        Optional<PasswordReset> passwordReset = passwordResetDao.findByResetToken("nonexistent-token");
        assertThat(passwordReset).isEmpty();
    }

    @Test
    @DataSet("datasets/password_reset.yml")
    @Transactional
    public void deleteByResetToken_正常にリセットトークンを削除できること() {
        passwordResetDao.deleteByResetToken("valid-token");

        Optional<PasswordReset> deletedPasswordReset = passwordResetDao.findByResetToken("valid-token");
        assertThat(deletedPasswordReset).isEmpty();
    }
}
