package jp.co.example.controller.form;

import jakarta.validation.constraints.Digits;

public class ContactSearchForm {
	
	@Digits(integer = 20, fraction = 0, message = "商品IDは数値で入力してください")
	private Long contactId;
	
	private String status;

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
