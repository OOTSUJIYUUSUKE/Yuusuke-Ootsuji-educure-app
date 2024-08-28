package jp.co.example.service;

import jp.co.example.dao.ProductDao;
import jp.co.example.dao.ProductImageDao;
import jp.co.example.entity.Product;
import jp.co.example.entity.ProductImage;
import jp.co.example.exception.ProductNotFoundException;
import jp.co.example.controller.form.ProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductImageDao productImageDao;

    @Mock
    private DropboxService dropboxService;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;
    private ProductForm productForm;

    @BeforeEach
    public void setUp() {
        product1 = new Product();
        product1.setProductId(1L);
        product1.setUserId("tanaka_123");
        product1.setProductName("トマト");
        product1.setDescription("新鮮なトマト");
        product1.setPrice(BigDecimal.valueOf(518.76));
        product1.setCreatedAt(LocalDateTime.of(2024, 8, 24, 8, 44, 53));
        product1.setSoldOut(false);

        product2 = new Product();
        product2.setProductId(2L);
        product2.setUserId("tanaka_123");
        product2.setProductName("キュウリ");
        product2.setDescription("瑞々しいキュウリ");
        product2.setPrice(BigDecimal.valueOf(64.35));
        product2.setCreatedAt(LocalDateTime.of(2024, 8, 23, 7, 44, 53));
        product2.setSoldOut(false);

        product3 = new Product();
        product3.setProductId(3L);
        product3.setUserId("yamada_901");
        product3.setProductName("キャベツ");
        product3.setDescription("柔らかいキャベツ");
        product3.setPrice(BigDecimal.valueOf(894.96));
        product3.setCreatedAt(LocalDateTime.of(2024, 8, 22, 6, 44, 53));
        product3.setSoldOut(false);

        productForm = new ProductForm();
        productForm.setUserId("tanaka_123");
        productForm.setProductName("トマト");
        productForm.setDescription("新鮮なトマト");
        productForm.setPrice(BigDecimal.valueOf(518.76));
        List<MultipartFile> imageData = Arrays.asList(mock(MultipartFile.class));
        productForm.setImageData(imageData);
    }

    @Test
    public void findTop10ByOrderByCreatedAtDesc_正常に最新の商品を取得できる() {
        doReturn(Arrays.asList(product1, product2, product3)).when(productDao).findTop10ByOrderByCreatedAtDesc();

        List<Product> result = productService.findTop10ByOrderByCreatedAtDesc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getCreatedAt()).isAfter(result.get(1).getCreatedAt());
        verify(productDao, times(1)).findTop10ByOrderByCreatedAtDesc();
    }

    @Test
    public void findProductsByUserIdOrderByCreatedAtDesc_正常にユーザーIDで商品を取得できる() {
        doReturn(Arrays.asList(product1, product2)).when(productDao).findProductsByUserIdOrderByCreatedAtDesc("tanaka_123");

        List<Product> result = productService.findProductsByUserIdOrderByCreatedAtDesc("tanaka_123");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCreatedAt()).isAfter(result.get(1).getCreatedAt());
        verify(productDao, times(1)).findProductsByUserIdOrderByCreatedAtDesc("tanaka_123");
    }

    @Test
    public void getProductsWithImages_正常に全ての商品を取得できる() {
        doReturn(Arrays.asList(product1, product2, product3)).when(productDao).findAll();

        List<Product> result = productService.getProductsWithImages();

        assertThat(result).hasSize(3);
        verify(productDao, times(1)).findAll();
    }

    @Test
    public void getProductsByName_部分一致で商品名が検索できる() {
        doReturn(Arrays.asList(product1)).when(productDao).findByProductNameContaining("トマト");

        List<Product> result = productService.getProductsByName("トマト");

        assertThat(result).hasSize(1);
        verify(productDao, times(1)).findByProductNameContaining("トマト");
    }

    @Test
    public void findProductById_商品が見つからない場合は例外をスローする() {
        doReturn(Optional.empty()).when(productDao).findById(1L);

        assertThrows(ProductNotFoundException.class, () -> {
            productService.findProductById(1L);
        });
    }

    @Test
    public void findProductById_正常に商品を取得できる() {
        doReturn(Optional.of(product1)).when(productDao).findById(1L);

        Product result = productService.findProductById(1L);

        assertThat(result).isEqualTo(product1);
        verify(productDao, times(1)).findById(1L);
    }

    @Test
    public void getProductNameById_商品名が正常に取得できる() {
        doReturn(Optional.of(product1)).when(productDao).findById(1L);

        String result = productService.getProductNameById(1L);

        assertThat(result).isEqualTo("トマト");
        verify(productDao, times(1)).findById(1L);
    }

    @Test
    public void updateStatus_商品ステータスを正常に更新できる() {
    	doReturn(product1).when(productDao).save(product1);

        productService.updateStatus(product1);

        verify(productDao, times(1)).save(product1);
    }

    @Test
    public void saveProduct_正常に商品を保存できる() throws IOException {   	
    	doReturn("http://dropbox/test_image").when(dropboxService).uploadFile(any());
    	
        productService.saveProduct(productForm);

        verify(productDao, times(1)).save(any(Product.class));
        verify(productImageDao, times(1)).save(any(ProductImage.class));
    }
}

