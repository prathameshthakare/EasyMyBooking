package com.learn.authserviceforproject.controller;

import com.learn.authserviceforproject.model.Place;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.service.UserOwnerService;
import com.learn.authserviceforproject.utility.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class OwnerServiceControllerTests {

    @InjectMocks
    private OwnerServiceController ownerServiceController;

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
    public void testGetAllOwners() {
        when(userOwnerService.getAllOwners()).thenReturn(List.of(new UserOwner()));

        ResponseEntity<?> response = ownerServiceController.getAllOwners();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetOwnerById_Success() throws Exception {
        String emailId = "test@example.com";
        String token = "Bearer validToken";
        UserOwner userOwner = new UserOwner();
        when(jwtUtil.validateToken(anyString(), eq(emailId), anyString())).thenReturn(true);
        when(userOwnerService.getUserOwnerByEmailId(emailId)).thenReturn(userOwner);

        ResponseEntity<?> response = ownerServiceController.getOwnerById(emailId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userOwner, response.getBody());
    }

    @Test
    public void testGetOwnerById_Unauthorized() throws Exception {
        String emailId = "test@example.com";
        String token = "Bearer invalidToken";
        when(jwtUtil.validateToken(anyString(), eq(emailId), anyString())).thenReturn(false);

        ResponseEntity<?> response = ownerServiceController.getOwnerById(emailId, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void testCreatePlace_Success() {
        String ownerEmailId = "owner@example.com";
        String token = "Bearer validToken";
        Place place = new Place();
        when(jwtUtil.validateToken(anyString(), eq(ownerEmailId), anyString())).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Place.class)))
                .thenReturn(new ResponseEntity<>(place, HttpStatus.OK));

        ResponseEntity<?> response = ownerServiceController.createPlace(place, token, ownerEmailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place, response.getBody());
    }

    @Test
    public void testUpdatePlace_Success() {
        int placeId = 1;
        String ownerEmailId = "owner@example.com";
        String token = "Bearer validToken";
        Place place = new Place();
        when(jwtUtil.validateToken(anyString(), eq(ownerEmailId), anyString())).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Place.class)))
                .thenReturn(new ResponseEntity<>(place, HttpStatus.OK));

        ResponseEntity<?> response = ownerServiceController.updatePlace(placeId, place, token, ownerEmailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place, response.getBody());
    }

    @Test
    public void testDeletePlace_Success() {
        int placeId = 1;
        String ownerEmailId = "owner@example.com";
        String token = "Bearer validToken";
        when(jwtUtil.validateToken(anyString(), eq(ownerEmailId), anyString())).thenReturn(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Place deleted successfully", HttpStatus.OK));

        ResponseEntity<?> response = ownerServiceController.deletePlace(placeId, token, ownerEmailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Place deleted successfully", response.getBody());
    }
}
