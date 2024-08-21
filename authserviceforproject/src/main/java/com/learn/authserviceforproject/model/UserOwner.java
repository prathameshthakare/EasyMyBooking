package com.learn.authserviceforproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "User_Owner_Master")
public class UserOwner {
    @Id
    @Column(length = 30)
    private String emailId;

    @Column(length = 30)
    private String username;

    public UserOwner() {
		super();
	}

	@Column(length = 30)
    private String password;

    @Column(length = 10)
    private String role;
    
    

	public UserOwner(String emailId, String username, String password, String role) {
		super();
		this.emailId = emailId;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
}
