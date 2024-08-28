package jp.co.example.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginForm {
    @NotBlank(message = "ユーザーIDは必須です")
    private String userId;

    @NotBlank(message = "パスワードは必須です")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/])[A-Za-z\\d!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]{8,}$", 
    message = "パスワードは8文字以上で、アルファベット、数字、記号を含める必要があります。")
    private String password;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
