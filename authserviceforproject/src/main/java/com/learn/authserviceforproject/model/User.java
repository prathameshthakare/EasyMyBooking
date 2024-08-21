package com.learn.authserviceforproject.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//@EqualsAndHashCode

@Data
//@Entity
//@Table(name = "User_Master")
public class User {
      @Id
      @Column(length = 30)
      private String emailId;
      @Column(length = 30)
	   private String username;
       @Column(length = 30)
	   private String password; 
        @Column(length = 10)
 	   private String role;
        
        
        
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
        
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
}
