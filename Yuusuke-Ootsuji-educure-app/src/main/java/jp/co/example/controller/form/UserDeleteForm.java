package jp.co.example.controller.form;

import jakarta.validation.constraints.NotBlank;

public class UserDeleteForm {

    @NotBlank(message = "パスワードを入力してください")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
