package jp.co.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.controller.form.ProductForm;
import jp.co.example.dao.ProductDao;
import jp.co.example.dao.ProductImageDao;
import jp.co.example.entity.Product;
import jp.co.example.entity.ProductImage;
import jp.co.example.exception.ProductNotFoundException;

@Service
public class ProductService {
	
	@Autowired
    private ProductDao productDao;
	
	@Autowired
    private ProductImageDao productImageDao;

    @Autowired
    private DropboxService dropboxService;
    
	public List<Product> findTop10ByOrderByCreatedAtDesc() {
        return productDao.findTop10ByOrderByCreatedAtDesc();
    }
	
	public List<Product> getProductsWithImages() {
        return productDao.findAll();
    }
	
	public List<Product> getProductsByName(String productName) {
        return productDao.findByProductNameContaining(productName);
    }
	
	public Product findProductById(Long productId) {
        return productDao.findById(productId).orElseThrow(() -> new ProductNotFoundException("商品が見つかりませんでした: ID = " + productId));
    }
	
	public List<Product> findProductsByUserIdOrderByCreatedAtDesc(String userId) {
        return productDao.findProductsByUserIdOrderByCreatedAtDesc(userId);
    }
	
	public String getProductNameById(Long productId) {
        Optional<Product> product = productDao.findById(productId);
        return product.map(Product::getProductName).orElse(null);
    }
	
	@Transactional
	public void updateStatus(Product product) {
        productDao.save(product);
    }
	
	@Transactional
	public void saveProduct(ProductForm productForm) throws IOException {
        Product product = new Product();
        product.setUserId(productForm.getUserId());
        product.setProductName(productForm.getProductName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());

        productDao.save(product);

        List<String> imageUrls = productForm.getImageData().stream()
            .map(multipartFile -> {
            	try {
                    String url = dropboxService.uploadFile(multipartFile);
                    if (url.contains("?dl=0")) {
                        url = url.replace("?dl=0", "?dl=1");
                    } else if (url.contains("&dl=0")) {
                        url = url.replace("&dl=0", "&dl=1");
                    }
                    return url;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            })
            .collect(Collectors.toList());

        for (String imageUrl : imageUrls) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageUrl(imageUrl);
            productImageDao.save(productImage);
        }
    }
}