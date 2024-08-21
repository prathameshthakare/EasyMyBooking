package com.learn.easeMyBooking.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class UserContext {
    private String emailId;
    private String role;
	public UserContext(String emailId, String role) {
		super();
		this.emailId = emailId;
		this.role = role;
	}

	public UserContext() {

	}

	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    
}