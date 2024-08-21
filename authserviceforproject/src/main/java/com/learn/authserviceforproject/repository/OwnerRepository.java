//package com.learn.authserviceforproject.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.learn.authserviceforproject.model.Owner;
//
////@Repository
//public interface OwnerRepository  extends  JpaRepository<Owner, String>{
//	
//	//@Query("select  u from user u where u.emailId=:emailId and u.password=:password")
//	public Optional<Owner> findByEmailIdAndPassword(String emailId, String password);
//
//	public Optional<Owner> findByEmailId(String emailId);
//
//}