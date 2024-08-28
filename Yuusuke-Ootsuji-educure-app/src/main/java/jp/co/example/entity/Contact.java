package jp.co.example.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private String status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "未対応";
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
	@Override
	public int hashCode() {
		return Objects.hash(contactId, createdAt, email, message, status, subject, updatedAt, userName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		return Objects.equals(contactId, other.contactId) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(email, other.email) && Objects.equals(message, other.message)
				&& Objects.equals(status, other.status) && Objects.equals(subject, other.subject)
				&& Objects.equals(updatedAt, other.updatedAt) && Objects.equals(userName, other.userName);
	}
}
