package jp.co.example.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class NewPasswordForm {

	@NotBlank(message = "パスワードは必須です")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
	         message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
	private String newPassword;

	@NotBlank(message = "パスワードの再入力は必須です")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
	         message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
	private String confirmPassword;

	private String token;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
