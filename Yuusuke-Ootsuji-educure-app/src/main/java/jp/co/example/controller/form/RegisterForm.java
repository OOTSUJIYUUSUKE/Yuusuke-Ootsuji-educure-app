package jp.co.example.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterForm {
	
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
    @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号は10桁または11桁の数字で入力してください")
    private String telephone;

    @NotBlank(message = "パスワードを入力してください")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
             message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
    private String password;

    @NotBlank(message = "パスワードを入力してください")
    private String confirmPassword;

	public RegisterForm() {
	}

	public RegisterForm(String userId, String userName, String email, String address, String telephone, String password, String confirmPassword) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.address = address;
		this.telephone = telephone;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
