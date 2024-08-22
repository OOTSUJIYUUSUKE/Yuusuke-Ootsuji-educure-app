package jp.co.example.controller.form;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {
    @NotBlank(message = "ユーザーIDは必須です")
    private String userId;

    @NotBlank(message = "パスワードは必須です")
    private String passwordHash;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
