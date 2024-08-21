package com.learn.authserviceforproject.service;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.learn.authserviceforproject.service.UserOwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.repository.UseOwnerRepository;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
class UserOwnerServiceImplTests {

    @Autowired
    private UserOwnerService userOwnerService;

    @MockBean
    private UseOwnerRepository useOwnerRepository;

    @Test
    void registerUserOwner_UserRegisteredSuccessfully() throws EmailIdAlreadyExistException {
        // Arrange
        UserOwner userOwner = new UserOwner();
        userOwner.setEmailId("test@example.com");
        userOwner.setRole("user");

        when(useOwnerRepository.findByEmailId(userOwner.getEmailId())).thenReturn(Optional.empty());
        when(useOwnerRepository.save(any(UserOwner.class))).thenReturn(userOwner);

        // Act
        UserOwner result = userOwnerService.registerUserOwner(userOwner);

        // Assert
        assertNotNull(result);
        assertEquals(userOwner.getEmailId(), result.getEmailId());
        assertEquals(userOwner.getRole(), result.getRole());
    }

    @Test
    void registerUserOwner_EmailIdAlreadyExists() throws EmailIdAlreadyExistException {
        // Arrange
        UserOwner userOwner = new UserOwner();
        userOwner.setEmailId("test@example.com");

        when(useOwnerRepository.findByEmailId(userOwner.getEmailId()))
                .thenReturn(Optional.of(userOwner));

        // Act & Assert
        assertThrows(EmailIdAlreadyExistException.class, () -> {
            userOwnerService.registerUserOwner(userOwner);
        });
    }
}

