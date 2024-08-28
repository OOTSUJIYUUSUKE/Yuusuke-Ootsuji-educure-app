package jp.co.example.controller.form;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jp.co.example.validator.NotEmptyMultipartFiles;

public class ProductForm {

	private Long productId;
    private String userId;
    @NotBlank(message = "商品名を入力してください")
    private String productName;
    @NotBlank(message = "商品説明を入力してください")
    private String description;
    @NotNull(message = "価格を入力してください")
    @Min(value = 0, message = "価格は0以上を設定してください")
    private BigDecimal price;
    @NotEmptyMultipartFiles(message = "少なくとも1つの画像を選択してください")
    private List<MultipartFile> imageData = new ArrayList<>();
    private LocalDateTime createdAt;

    public ProductForm() {
    }

    public ProductForm(Long productId, String userId, String productName, String description, BigDecimal price,
            List<MultipartFile> imageData, LocalDateTime createdAt) {
        this.productId = productId;
        this.userId = userId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.imageData = imageData;
        this.createdAt = createdAt;
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public List<MultipartFile> getImageData() {
        return imageData;
    }
    public void setImageData(List<MultipartFile> imageData) {
        this.imageData = imageData;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
