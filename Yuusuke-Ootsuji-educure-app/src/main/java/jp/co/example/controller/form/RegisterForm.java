package jp.co.example.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterForm {
	
	@NotBlank(message = "ユーザーIDは必須です")
    private String userId;

    @NotBlank(message = "ユーザー名は必須です")
    private String userName;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "無効なメールアドレスです")
    private String email;

    @NotBlank(message = "住所は必須です")
    private String address;

    @NotBlank(message = "電話番号は必須です")
    private String telephone;

    @NotBlank(message = "パスワードは必須です")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
             message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
    private String passwordHash;

    @NotBlank(message = "パスワードの再入力は必須です")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
             message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
    private String confirmPassword;

	public RegisterForm() {
	}

	public RegisterForm(String userId, String userName, String email, String address, String telephone, String passwordHash, String confirmPassword) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.address = address;
		this.telephone = telephone;
		this.passwordHash = passwordHash;
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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
    
}
