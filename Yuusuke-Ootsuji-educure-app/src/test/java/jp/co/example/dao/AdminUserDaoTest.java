package jp.co.example.dao;

import jp.co.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminUserDaoTest {

    @Autowired
    private AdminUserDao adminUserDao;

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserIdOrEmail_ユーザーIDで検索してユーザーが取得できる() {
        List<User> users = adminUserDao.findByUserIdOrEmail("tanaka_123", "");
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUserId()).isEqualTo("tanaka_123");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserIdOrEmail_メールで検索してユーザーが取得できる() {
        List<User> users = adminUserDao.findByUserIdOrEmail("", "sato_67890@example.com");
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getEmail()).isEqualTo("sato_67890@example.com");
    }
    
    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserIdOrEmail_ユーザーIDとメールで検索してユーザーが取得できる() {
        List<User> users = adminUserDao.findByUserIdOrEmail("tanaka_123", "sato_67890@example.com");
        assertThat(users).isNotEmpty();
        boolean hasTanaka123 = users.stream()
                                    .anyMatch(user -> "tanaka_123".equals(user.getUserId()));
        boolean hasSato67890 = users.stream()
                                    .anyMatch(user -> "sato_67890@example.com".equals(user.getEmail()));        
        assertThat(hasTanaka123).isTrue();
        assertThat(hasSato67890).isTrue();
    }


    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserId_正常にユーザーIDでユーザーが取得できる() {
        User user = adminUserDao.findByUserId("tanaka_123");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("tanaka_123");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void deleteByUserId_正常にユーザーを削除できる() {
        adminUserDao.deleteByUserId("tanaka_123");

        User deletedUser = adminUserDao.findByUserId("tanaka_123");
        assertThat(deletedUser).isNull();
    }
}
