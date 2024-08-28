package jp.co.example.dao;

import jp.co.example.entity.Product;
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
public class AdminProductDaoTest {

    @Autowired
    private AdminProductDao adminProductDao;

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findByProductIdOrUserId_商品IDまたはユーザーIDで検索して商品が取得できる() {
        List<Product> products = adminProductDao.findByProductIdOrUserId(1L, "tanaka_123");
        assertThat(products).isNotEmpty();        
        boolean hasProductWithId = products.stream()
                                           .anyMatch(product -> 1L == product.getProductId());
        boolean hasProductWithUserId = products.stream()
                                               .anyMatch(product -> "tanaka_123".equals(product.getUserId()));       
        assertThat(hasProductWithId).isTrue();
        assertThat(hasProductWithUserId).isTrue();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findByProductId_商品IDで商品が取得できる() {
        Product product = adminProductDao.findByProductId(1L);
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isEqualTo(1L);
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findByProductId_存在しない商品IDでnullを返す() {
        Product product = adminProductDao.findByProductId(999L);
        assertThat(product).isNull();
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void deleteByProductId_商品IDで商品を削除できる() {
        adminProductDao.deleteByProductId(1L);
        Product deletedProduct = adminProductDao.findByProductId(1L);
        assertThat(deletedProduct).isNull();
    }
}
