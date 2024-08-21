//package com.learn.authserviceforproject.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
//import com.learn.authserviceforproject.model.Owner;
//import com.learn.authserviceforproject.model.User;
//
//@Repository
//public interface UserRepository  extends  JpaRepository<User, String>{
//	
//	//@Query("select  u from user u where u.emailId=:emailId and u.password=:password")
//	public Optional<User> findByEmailIdAndPassword(String emailId, String password);
//	Owner getOwnerByEmailId(String emailId) throws EmailIdNotExistException;
//
//}