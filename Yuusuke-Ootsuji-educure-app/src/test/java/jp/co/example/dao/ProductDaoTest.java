package jp.co.example.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import jp.co.example.entity.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findTop10ByOrderByCreatedAtDesc_正常に最新の商品を取得できる() {
        List<Product> products = productDao.findTop10ByOrderByCreatedAtDesc();
        assertThat(products).hasSize(10);

        for (int i = 0; i < products.size() - 1; i++) {
            assertThat(products.get(i).getCreatedAt()).isAfterOrEqualTo(products.get(i + 1).getCreatedAt());
        }
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findByProductNameContaining_部分一致で商品名が検索できる() {
        List<Product> products = productDao.findByProductNameContaining("トマト");
        assertThat(products).isNotEmpty();
        for (Product product : products) {
            assertThat(product.getProductName()).contains("トマト");
        }
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findProductsByUserIdOrderByCreatedAtDesc_特定ユーザーの商品のみを取得できる() {
        List<Product> products = productDao.findProductsByUserIdOrderByCreatedAtDesc("tanaka_123");
        assertThat(products).isNotEmpty();
        for (Product product : products) {
            assertThat(product.getUserId()).isEqualTo("tanaka_123");
        }
    }

    @Test
    @DataSet({"datasets/role.yml", "datasets/users.yml", "datasets/products.yml"})
    @Transactional
    public void findProductsByUserIdOrderByCreatedAtDesc_結果が空のときに正しく動作する() {
        List<Product> products = productDao.findProductsByUserIdOrderByCreatedAtDesc("nonexistent_user");
        assertThat(products).isEmpty();
    }
}
