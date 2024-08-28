package jp.co.example.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserEditForm {
	@NotBlank(message = "ユーザーIDを入力してください")
	private String userId;
	@NotBlank(message = "ユーザー名を入力してください")
	private String userName;
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "無効なメールアドレスです")
	private String email;
	@NotBlank(message = "住所を入力してください")
	private String address;
	@NotBlank(message = "電話番号を入力してください")
	@Pattern(regexp = "^[0-9]{10,11}$", message = "無効な電話番号です")
	private String telephone;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
