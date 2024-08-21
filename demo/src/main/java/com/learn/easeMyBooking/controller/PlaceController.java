package com.learn.easeMyBooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learn.easeMyBooking.model.Place;
import com.learn.easeMyBooking.Exception.DuplicatePlaceNameException;
import com.learn.easeMyBooking.context.UserContext;
import com.learn.easeMyBooking.service.PlaceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/easeMyBooking")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    private UserContext getUserContext(HttpServletRequest request) {
        return (UserContext) request.getAttribute("userContext");
    }

    private ResponseEntity<String> checkAuthorization(UserContext userContext, String expectedEmailId, String expectedRole) {
        String emailId = userContext.getEmailId();
        String role = userContext.getRole();
        if (!emailId.equalsIgnoreCase(expectedEmailId) || !role.equalsIgnoreCase(expectedRole)) {
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("user/{userEmailId}/place")
    public ResponseEntity<?> getAllPlaces(@PathVariable String userEmailId, HttpServletRequest request) {
        UserContext userContext = getUserContext(request);

        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "user");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        List<Place> placeList = placeService.getAllPlaces();
        return new ResponseEntity<>(placeList, HttpStatus.OK);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<?> getPlaceById(@PathVariable int placeId) {
        Place place = placeService.getPlaceById(placeId);
        return place != null ? new ResponseEntity<>(place, HttpStatus.OK) : new ResponseEntity<>("Place not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/places/owner/{ownerEmailId}")
    public ResponseEntity<?> getPlaceByOwnerEmailId(@PathVariable String ownerEmailId, HttpServletRequest request) {
    	
        UserContext userContext = getUserContext(request);

        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, ownerEmailId, "owner");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        List<Place> placeList = placeService.getPlacesByOwnerEmailId(ownerEmailId);
        return placeList != null ? new ResponseEntity<>(placeList, HttpStatus.OK) : new ResponseEntity<>("Owner emailId not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{ownerEmailId}/place")
    public ResponseEntity<?> addPlace(@RequestBody Place place, @PathVariable String ownerEmailId, HttpServletRequest request) {
    	
        UserContext userContext = getUserContext(request);

        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, ownerEmailId, "owner");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }
        
        place.setOwnerEmailId(ownerEmailId);

        try {
            Place savedPlace = placeService.addPlace(place);
            return new ResponseEntity<>(savedPlace, HttpStatus.OK);
        } catch (DuplicatePlaceNameException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @DeleteMapping("/{ownerEmailId}/place/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id, @PathVariable String ownerEmailId, HttpServletRequest request) {
        UserContext userContext = getUserContext(request);

        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, ownerEmailId, "owner");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        boolean isDeleted = placeService.deletePlace(id);
        return isDeleted ? new ResponseEntity<>("Place is deleted successfully", HttpStatus.OK) : new ResponseEntity<>("Place Id Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("{ownerEmailId}/place/{placeId}")
    public ResponseEntity<?> updatePlace(@PathVariable int placeId, @RequestBody Place place, @PathVariable String ownerEmailId, HttpServletRequest request) {
        UserContext userContext = getUserContext(request);

        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, ownerEmailId, "owner");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        if (!placeService.getPlaceById(placeId).getOwnerEmailId().equals(ownerEmailId)) {
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        }
        place.setPlaceId(placeId);
        place.setOwnerEmailId(ownerEmailId);
        Place updatedPlace = placeService.updatePlace(placeId, place);
        return updatedPlace != null ? new ResponseEntity<>(updatedPlace, HttpStatus.OK) : new ResponseEntity<>("Place not found", HttpStatus.NOT_FOUND);
    }
}
