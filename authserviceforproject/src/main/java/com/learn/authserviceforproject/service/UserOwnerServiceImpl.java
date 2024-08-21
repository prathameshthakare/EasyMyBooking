package com.learn.authserviceforproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.repository.UseOwnerRepository;

@Service
public class UserOwnerServiceImpl implements UserOwnerService {

    @Autowired
    private UseOwnerRepository useOwnerRepository;

    @Override
    public UserOwner registerUserOwner(UserOwner useOwner) throws EmailIdAlreadyExistException {
        Optional<UserOwner> existingUseOwner = useOwnerRepository.findByEmailId(useOwner.getEmailId());
        if (existingUseOwner.isPresent()) {
            throw new EmailIdAlreadyExistException("Email ID already exists: " + useOwner.getEmailId());
        }
        return useOwnerRepository.save(useOwner);
    }

    @Override
    public UserOwner getUserOwnerByEmailId(String emailId) throws EmailIdNotExistException {
        return useOwnerRepository.findByEmailId(emailId)
                .orElseThrow(() -> new EmailIdNotExistException("Email ID does not exist: " + emailId));
    }

    @Override
    public UserOwner updateUserOwner(UserOwner useOwner) throws EmailIdNotExistException {
        if (!useOwnerRepository.existsById(useOwner.getEmailId())) {
            throw new EmailIdNotExistException("Email ID does not exist: " + useOwner.getEmailId());
        }
        return useOwnerRepository.save(useOwner);
    }

    @Override
    public boolean deleteUserOwner(String emailId) {
        if (!useOwnerRepository.existsById(emailId)) {
            return false;
        }
        useOwnerRepository.deleteById(emailId);
        return true;
    }

    @Override
    public List<UserOwner> getAllUserOwners() {
        return useOwnerRepository.findAll();
    }

    @Override
    public boolean validateUserOwner(UserOwner useOwner) {
        Optional<UserOwner> foundUseOwner = useOwnerRepository.findByEmailIdAndPasswordAndRole(useOwner.getEmailId(), useOwner.getPassword(), useOwner.getRole());
        return foundUseOwner.isPresent();
    }

    @Override
    public List<UserOwner> getAllUsers() {
        return useOwnerRepository.findByRole("user");
    }

    @Override
    public List<UserOwner> getAllOwners() {
        return useOwnerRepository.findByRole("owner");
    }
}
