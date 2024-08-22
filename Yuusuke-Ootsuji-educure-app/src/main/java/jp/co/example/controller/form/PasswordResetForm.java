package jp.co.example.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetForm {

    @NotBlank(message = "メールアドレスは必須です。")
    @Email(message = "無効なメールアドレスです。")
    private String email;

    // Getter と Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
