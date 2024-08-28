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

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminDaoTest {

    @Autowired
    private AdminDao adminDao;

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserId_ユーザーIDでユーザーが取得できる() {
        User user = adminDao.findByUserId("tanaka_123");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("tanaka_123");
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void findByUserId_存在しないユーザーIDでnullを返す() {
        User user = adminDao.findByUserId("nonexistent_user");
        assertThat(user).isNull();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByUserId_存在するユーザーIDでtrueを返す() {
        boolean exists = adminDao.existsByUserId("tanaka_123");
        assertThat(exists).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByUserId_存在しないユーザーIDでfalseを返す() {
        boolean exists = adminDao.existsByUserId("nonexistent_user");
        assertThat(exists).isFalse();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByTelephone_存在する電話番号でtrueを返す() {
        boolean exists = adminDao.existsByTelephone("09012345678");
        assertThat(exists).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml"})
    @Transactional
    public void existsByTelephone_存在しない電話番号でfalseを返す() {
        boolean exists = adminDao.existsByTelephone("08011112222");
        assertThat(exists).isFalse();
    }
}
