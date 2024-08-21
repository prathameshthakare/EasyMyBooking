package com.learn.authserviceforproject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.Place;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.service.UserOwnerService;
import com.learn.authserviceforproject.utility.JwtUtil;

class UserOwnerServiceControllerTests {

    @InjectMocks
    private UserOwnerServiceController controller;

    @Mock
    private UserOwnerService userOwnerService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void registerOwner_UserRegisteredSuccessfully() throws EmailIdAlreadyExistException {
        // Arrange
        UserOwner userOwner = new UserOwner();
        userOwner.setEmailId("test@example.com");
        userOwner.setRole("user");

        // Mock the service call to return the UserOwner object
        when(userOwnerService.registerUserOwner(any(UserOwner.class))).thenReturn(userOwner);

        // Act
        ResponseEntity<?> responseEntity = controller.registerOwner(userOwner);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User Registered Successfully...", responseEntity.getBody());
    }




    @Test
    void registerOwner_EmailIdAlreadyExists() throws EmailIdAlreadyExistException {
        UserOwner userOwner = new UserOwner();
        doThrow(new EmailIdAlreadyExistException("Email already exists")).when(userOwnerService).registerUserOwner(userOwner);

        ResponseEntity<?> response = controller.registerOwner(userOwner);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void validateOwner_ValidUserCredentials() {
        UserOwner userOwner = new UserOwner();
        userOwner.setEmailId("user@example.com");
        userOwner.setRole("user");
        when(userOwnerService.validateUserOwner(userOwner)).thenReturn(true);
        when(jwtUtil.getToken("user@example.com", "user")).thenReturn("sampleToken");

        ResponseEntity<?> response = controller.validateOwner(userOwner);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sampleToken", response.getBody());
    }

    @Test
    void validateOwner_InvalidUserCredentials() {
        UserOwner userOwner = new UserOwner();
        when(userOwnerService.validateUserOwner(userOwner)).thenReturn(false);

        ResponseEntity<?> response = controller.validateOwner(userOwner);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid User Credentials...", response.getBody());
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        List<UserOwner> userOwners = Arrays.asList(new UserOwner(), new UserOwner());
        when(userOwnerService.getAllUsers()).thenReturn(userOwners);

        ResponseEntity<?> response = controller.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userOwners, response.getBody());
    }

    @Test
    void getUserById_UserExists_ValidToken() throws EmailIdNotExistException {
        String emailId = "user@example.com";
        String token = "Bearer sampleToken";
        String extractedToken = "sampleToken";
        UserOwner userOwner = new UserOwner();
        when(jwtUtil.validateToken(extractedToken, emailId, "user")).thenReturn(true);
        when(userOwnerService.getUserOwnerByEmailId(emailId)).thenReturn(userOwner);

        ResponseEntity<?> response = controller.getUserById(emailId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userOwner, response.getBody());
    }

    @Test
    void getUserById_InvalidToken() throws EmailIdNotExistException {
        String emailId = "user@example.com";
        String token = "Bearer sampleToken";
        String extractedToken = "sampleToken";
        when(jwtUtil.validateToken(extractedToken, emailId, "user")).thenReturn(false);

        ResponseEntity<?> response = controller.getUserById(emailId, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    void updateUser_UserUpdatedSuccessfully() throws EmailIdNotExistException {
        String emailId = "user@example.com";
        String token = "Bearer sampleToken";
        String extractedToken = "sampleToken";
        UserOwner user = new UserOwner();
        UserOwner updatedUser = new UserOwner();
        when(jwtUtil.validateToken(extractedToken, emailId, "user")).thenReturn(true);
        when(userOwnerService.updateUserOwner(user)).thenReturn(updatedUser);

        ResponseEntity<?> response = controller.updateUser(emailId, user, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void deleteUser_UserDeletedSuccessfully() throws EmailIdNotExistException {
        String emailId = "user@example.com";
        String token = "Bearer sampleToken";
        String extractedToken = "sampleToken";
        when(jwtUtil.validateToken(extractedToken, emailId, "user")).thenReturn(true);
        when(userOwnerService.deleteUserOwner(emailId)).thenReturn(true);

        ResponseEntity<?> response = controller.deleteUser(emailId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with id: " + emailId + " deleted successfully", response.getBody());
    }

    @Test
    void deleteUser_EmailIdNotExist() throws EmailIdNotExistException {
        String emailId = "user@example.com";
        String token = "Bearer sampleToken";
        String extractedToken = "sampleToken";
        when(jwtUtil.validateToken(extractedToken, emailId, "user")).thenReturn(true);
        when(userOwnerService.deleteUserOwner(emailId)).thenReturn(false);

        ResponseEntity<?> response = controller.deleteUser(emailId, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email Id Not Exist", response.getBody());
    }

    @Test
    void getAllPlaces_ValidResponse() {
        String userEmailId = "user@example.com";
        String token = "Bearer sampleToken";
        Place[] places = new Place[] { new Place(), new Place() };
        String url = "http://localhost:9093/api/easeMyBooking/user/" + userEmailId + "/place";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        ResponseEntity<Place[]> responseEntity = new ResponseEntity<>(places, HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(Place[].class))).thenReturn(responseEntity);

        ResponseEntity<?> response = controller.getAllPlaces(token, userEmailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(places), response.getBody());
    }

    @Test
    void getAllPlaces_PlaceNotFound() {
        String userEmailId = "user@example.com";
        String token = "Bearer sampleToken";
        String url = "http://localhost:9093/api/easeMyBooking/user/" + userEmailId + "/place";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        ResponseEntity<Place[]> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(Place[].class))).thenReturn(responseEntity);

        ResponseEntity<?> response = controller.getAllPlaces(token, userEmailId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Owner Id not found", response.getBody());
    }
}
