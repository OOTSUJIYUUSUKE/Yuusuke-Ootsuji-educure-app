package jp.co.example.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jp.co.example.entity.Order;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findBySessionId_正常にセッションIDで注文を取得できること() {
        Order order = orderDao.findBySessionId("session12345");
        assertThat(order).isNotNull();
        assertThat(order.getSessionId()).isEqualTo("session12345");
    }

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findBySessionId_存在しないセッションIDでnullを返す() {
        Order order = orderDao.findBySessionId("nonexistent_session");
        assertThat(order).isNull();
    }


    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findOrdersByUserIdOrderByCreatedAtDesc_正常にユーザーIDで注文を取得できること() {
        List<Order> orders = orderDao.findOrdersByUserIdOrderByCreatedAtDesc("tanaka_123");
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getUserId()).isEqualTo("tanaka_123");
    }

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findOrdersByUserIdOrderByCreatedAtDesc_存在しないユーザーIDで結果が空になること() {
        List<Order> orders = orderDao.findOrdersByUserIdOrderByCreatedAtDesc("nonexistent_user");
        assertThat(orders).isEmpty();
    }
}
