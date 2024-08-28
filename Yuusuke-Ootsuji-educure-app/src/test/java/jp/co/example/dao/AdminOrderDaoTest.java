package jp.co.example.dao;

import jp.co.example.entity.Order;
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
public class AdminOrderDaoTest {

    @Autowired
    private AdminOrderDao adminOrderDao;

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findByOrderId_正常にオーダーIDで検索できる() {
        List<Order> orders = adminOrderDao.findByOrderId(1L);
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getOrderId()).isEqualTo(1L);
    }

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void findByOrderId_存在しないオーダーIDで空のリストを返す() {
        List<Order> orders = adminOrderDao.findByOrderId(999L);
        assertThat(orders).isEmpty();
    }

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void getOrderByOrderId_正常に1件のオーダーを取得できる() {
        Order order = adminOrderDao.getOrderByOrderId(1L);
        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(1L);
    }

    @Test
    @DataSet("datasets/orders.yml")
    @Transactional
    public void getOrderByOrderId_存在しないオーダーIDでnullを返す() {
        Order order = adminOrderDao.getOrderByOrderId(999L);
        assertThat(order).isNull();
    }
}
