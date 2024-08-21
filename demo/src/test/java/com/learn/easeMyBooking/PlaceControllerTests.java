package com.learn.easeMyBooking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.learn.easeMyBooking.context.UserContext;
import com.learn.easeMyBooking.controller.PlaceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.learn.easeMyBooking.Exception.DuplicatePlaceNameException;
import com.learn.easeMyBooking.model.Place;
import com.learn.easeMyBooking.service.PlaceService;
import jakarta.servlet.http.HttpServletRequest;

public class PlaceControllerTests {

    @Mock
    private PlaceService placeService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PlaceController placeController;

    private UserContext userContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userContext = new UserContext();
        when(request.getAttribute("userContext")).thenReturn(userContext);
    }

    @Test
    void getAllPlaces_UserAuthorized() {
        // Arrange
        userContext.setEmailId("testuser@example.com");
        userContext.setRole("user");

        when(placeService.getAllPlaces()).thenReturn(Arrays.asList(new Place(), new Place()));

        // Act
        ResponseEntity<?> response = placeController.getAllPlaces("testuser@example.com", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(placeService, times(1)).getAllPlaces();
    }

    @Test
    void getAllPlaces_UserNotAuthorized() {
        // Arrange
        userContext.setEmailId("testuser@example.com");
        userContext.setRole("user");

        // Act
        ResponseEntity<?> response = placeController.getAllPlaces("wronguser@example.com", request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(placeService, never()).getAllPlaces();
    }

    @Test
    void addPlace_OwnerAuthorized_PlaceAddedSuccessfully() throws DuplicatePlaceNameException {
        // Arrange
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        Place place = new Place();
        when(placeService.addPlace(any(Place.class))).thenReturn(place);

        // Act
        ResponseEntity<?> response = placeController.addPlace(place, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(placeService, times(1)).addPlace(any(Place.class));
    }

    @Test
    void addPlace_OwnerNotAuthorized() {
        // Arrange
        userContext.setEmailId("wrongowner@example.com");
        userContext.setRole("owner");

        Place place = new Place();

        // Act
        ResponseEntity<?> response = placeController.addPlace(place, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(placeService, never()).addPlace(any(Place.class));
    }

    @Test
    void addPlace_DuplicatePlaceNameException() throws DuplicatePlaceNameException {
        // Arrange
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        Place place = new Place();
        when(placeService.addPlace(any(Place.class))).thenThrow(new DuplicatePlaceNameException("Place name already exists"));

        // Act
        ResponseEntity<?> response = placeController.addPlace(place, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Place name already exists", response.getBody());
    }

    @Test
    void deletePlace_OwnerAuthorized_PlaceDeletedSuccessfully() {
        // Arrange
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        when(placeService.deletePlace(anyInt())).thenReturn(true);

        // Act
        ResponseEntity<?> response = placeController.deletePlace(1, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Place is deleted successfully", response.getBody());
        verify(placeService, times(1)).deletePlace(anyInt());
    }

    @Test
    void deletePlace_OwnerNotAuthorized() {
        // Arrange
        userContext.setEmailId("wrongowner@example.com");
        userContext.setRole("owner");

        // Act
        ResponseEntity<?> response = placeController.deletePlace(1, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(placeService, never()).deletePlace(anyInt());
    }

    @Test
    void updatePlace_OwnerAuthorized_PlaceUpdatedSuccessfully() {
        // Arrange
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        Place place = new Place();
        place.setOwnerEmailId("owner@example.com");

        when(placeService.getPlaceById(anyInt())).thenReturn(place);
        when(placeService.updatePlace(anyInt(), any(Place.class))).thenReturn(place);

        // Act
        ResponseEntity<?> response = placeController.updatePlace(1, place, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(placeService, times(1)).updatePlace(anyInt(), any(Place.class));
    }

    @Test
    void updatePlace_OwnerNotAuthorized() {
        // Arrange
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        Place place = new Place();
        place.setOwnerEmailId("differentowner@example.com");

        when(placeService.getPlaceById(anyInt())).thenReturn(place);

        // Act
        ResponseEntity<?> response = placeController.updatePlace(1, place, "owner@example.com", request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(placeService, never()).updatePlace(anyInt(), any(Place.class));
    }
}
