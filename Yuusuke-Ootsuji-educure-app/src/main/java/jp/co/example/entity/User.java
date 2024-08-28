package jp.co.example.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	private String userId;
    private String userName;
    private String telephone;
    private String address;
    private String email;
    private String password;
    
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "roleId")
    private Role role;

    public User() {
    }

    public User(String userId, String userName, String telephone, String address, String email, String password, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getRoleName() {
        return (role != null) ? role.getRoleName() : null;
    }

	@Override
	public int hashCode() {
		return Objects.hash(address, email, password, role, telephone, userId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(address, other.address) && Objects.equals(email, other.email)
				&& Objects.equals(password, other.password) && Objects.equals(role, other.role)
				&& Objects.equals(telephone, other.telephone) && Objects.equals(userId, other.userId)
				&& Objects.equals(userName, other.userName);
	}
}