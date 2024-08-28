package jp.co.example.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactForm {
	
	@NotBlank(message = "ユーザー名を入力してください")
	private String userName;
	@NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "無効なメールアドレスです")
	private String email;
	@NotBlank(message = "件名を入力してください")
	private String subject;
	@NotBlank(message = "お問い合わせ内容を入力してください")
	private String message;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
