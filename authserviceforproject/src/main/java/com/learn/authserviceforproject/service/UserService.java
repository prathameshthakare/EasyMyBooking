package com.learn.authserviceforproject.service;

import java.util.List;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.User;

public interface UserService {
	
	public  User  registerUser(User  user) throws EmailIdAlreadyExistException;
	
	public User  getUserByEmailId(String emailId) throws EmailIdNotExistException;
	
	public User  updateUser(User user) throws EmailIdNotExistException;
	public  boolean  deleteUser(String emailId);
	public List<User> getAllUser();
	public boolean validateUser(User user);
}