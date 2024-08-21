package com.learn.authserviceforproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.UserOwner;

@Repository
public interface UseOwnerRepository extends JpaRepository<UserOwner, String> {

    Optional<UserOwner> findByEmailIdAndPassword(String emailId, String password);
    Optional<UserOwner> findByEmailIdAndPasswordAndRole(String emailId, String password, String role);

    Optional<UserOwner> findByEmailId(String emailId);

    List<UserOwner> findByRole(String role);
    
    default UserOwner getUserOwnerByEmailId(String emailId) throws EmailIdNotExistException {
        return findByEmailId(emailId)
                .orElseThrow(() -> new EmailIdNotExistException("Email ID does not exist: " + emailId));
    }
}
