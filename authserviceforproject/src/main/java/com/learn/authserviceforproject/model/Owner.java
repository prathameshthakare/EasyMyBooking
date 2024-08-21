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
//@Table(name = "Owner_Master")
public class Owner {
      @Id
      @Column(length = 30)
       private String emailId;
      
      //private int ownerId;
      
        @Column(length = 30)
	   private String password;
        @Column(length = 30)
	   private String ownername;
       // @Column(length = 15)
//	   private String mobileNumber;
//        @Column(length = 10)
//	   private String gender;
//        
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
		public String getOwnername() {
			return ownername;
		}
		public void setOwnername(String ownername) {
			this.ownername = ownername;
		}
//		public String getMobileNumber() {
//			return mobileNumber;
//		}
//		public void setMobileNumber(String mobileNumber) {
//			this.mobileNumber = mobileNumber;
//		}
//		public String getGender() {
//			return gender;
//		}
//		public void setGender(String gender) {
//			this.gender = gender;
//		}
//        

}

