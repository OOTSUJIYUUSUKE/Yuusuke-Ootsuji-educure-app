package jp.co.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "password_reset")
public class PasswordReset {

    @Id
    private String resetToken;

    private String userId;

    private LocalDateTime expiresAt;

    // Getters and Setters
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

	@Override
	public int hashCode() {
		return Objects.hash(expiresAt, resetToken, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordReset other = (PasswordReset) obj;
		return Objects.equals(expiresAt, other.expiresAt) && Objects.equals(resetToken, other.resetToken)
				&& Objects.equals(userId, other.userId);
	}
    
    
}
