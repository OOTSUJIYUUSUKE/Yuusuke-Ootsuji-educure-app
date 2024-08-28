package jp.co.example.controller.form;

import jakarta.validation.constraints.Digits;

public class ProductSearchForm {
	
	@Digits(integer = 20, fraction = 0, message = "商品IDは数値で入力してください")
	private Long productId;
	private String userId;

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

}
