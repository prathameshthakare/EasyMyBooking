package com.learn.authserviceforproject.service;

import java.util.List;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.Owner;

public interface OwnerService {
	
	public  Owner  registerOwner(Owner  owner) throws EmailIdAlreadyExistException;
	
	public Owner  getUserByEmailId(String emailId) throws EmailIdNotExistException;
	
	public Owner  updateOwner(Owner owner) throws EmailIdNotExistException;
	public  boolean  deleteOwner(String emailId);
	public List<Owner> getAllOwner();
	public boolean validateOwner(Owner owner);
}
