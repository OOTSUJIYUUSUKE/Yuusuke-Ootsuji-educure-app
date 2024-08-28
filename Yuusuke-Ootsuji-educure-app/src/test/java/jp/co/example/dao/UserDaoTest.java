package jp.co.example.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jp.co.example.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDaoTest {
    
    @Autowired
    private UserDao userDao;

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserId_正常なuserIdでユーザーが取得できる() {
        User user = userDao.findByUserId("tanaka_123");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("tanaka_123");
        assertThat(user.getUserName()).isEqualTo("田中太郎");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserId_存在しないuserIdでnullを返す() {
        User user = userDao.findByUserId("nonexistent_user");
        assertThat(user).isNull();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByUserId_存在するuserIdでtrueを返す() {
        boolean exists = userDao.existsByUserId("tanaka_123");
        assertThat(exists).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByUserId_存在しないuserIdでfalseを返す() {
        boolean exists = userDao.existsByUserId("nonexistent_user");
        assertThat(exists).isFalse();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByTelephone_存在する電話番号でtrueを返す() {
        boolean exists = userDao.existsByTelephone("09012345678");
        assertThat(exists).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByTelephone_存在しない電話番号でfalseを返す() {
        boolean exists = userDao.existsByTelephone("08011112222");
        assertThat(exists).isFalse();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByEmail_存在するメールアドレスでtrueを返す() {
        boolean exists = userDao.existsByEmail("tanaka_12345@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByEmail_存在しないメールアドレスでfalseを返す() {
        boolean exists = userDao.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByEmail_存在するメールアドレスでユーザーが取得できる() {
        Optional<User> user = userDao.findByEmail("tanaka_12345@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("tanaka_12345@example.com");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByEmail_存在しないメールアドレスで空の結果を返す() {
        Optional<User> user = userDao.findByEmail("nonexistent@example.com");
        assertThat(user).isNotPresent();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByTelephone_存在する電話番号でユーザーが取得できる() {
        Optional<User> user = userDao.findByTelephone("09012345678");
        assertThat(user).isPresent();
        assertThat(user.get().getTelephone()).isEqualTo("09012345678");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByTelephone_存在しない電話番号で空の結果を返す() {
        Optional<User> user = userDao.findByTelephone("08011112222");
        assertThat(user).isNotPresent();
    }
}
