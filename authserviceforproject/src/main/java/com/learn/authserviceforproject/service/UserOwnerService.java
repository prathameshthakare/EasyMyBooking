package com.learn.authserviceforproject.service;

import java.util.List;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.UserOwner;

public interface UserOwnerService {

    UserOwner registerUserOwner(UserOwner useOwner) throws EmailIdAlreadyExistException;

    UserOwner getUserOwnerByEmailId(String emailId) throws EmailIdNotExistException;

    UserOwner updateUserOwner(UserOwner useOwner) throws EmailIdNotExistException;

    boolean deleteUserOwner(String emailId);

    List<UserOwner> getAllUserOwners();

    boolean validateUserOwner(UserOwner useOwner);

    List<UserOwner> getAllUsers();

    List<UserOwner> getAllOwners();
}
