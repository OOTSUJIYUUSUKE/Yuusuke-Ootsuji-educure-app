package jp.co.example.controller.form;

import jakarta.validation.constraints.Digits;

public class OrderSearchForm {
	
	@Digits(integer = 20, fraction = 0, message = "商品IDは数値で入力してください")
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	
}
