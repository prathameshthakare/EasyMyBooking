package com.learn.authserviceforproject.controller;
import com.learn.authserviceforproject.utility.JwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

//import com.github.andrewoma.dexx.collection.ArrayList;
import com.learn.authserviceforproject.dto.UserBookingsResponse;
import com.learn.authserviceforproject.exceptions.DuplicatePlaceNameException;
import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.Booking;
import com.learn.authserviceforproject.model.Owner;
import com.learn.authserviceforproject.model.Place;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.service.OwnerService;
import com.learn.authserviceforproject.service.UserOwnerService;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class OwnerServiceController {

	@Autowired
	private UserOwnerService userOwnerService;
	
	
	
	@Autowired
	private RestTemplate  restTemplate;

	@Autowired
	JwtUtil jwtUtil;
	
	@GetMapping("/owner")
	public ResponseEntity<?> getAllOwners() {
		return new ResponseEntity<>(userOwnerService.getAllOwners(), HttpStatus.OK);
	}
	
	@GetMapping("/owner/{emailId}")
	public ResponseEntity<?> getOwnerById(@PathVariable String emailId,@RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		emailId = emailId.replace("%40", "@");
		System.out.println(emailId);
		
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		UserOwner userOwner = userOwnerService.getUserOwnerByEmailId(emailId);
		return new ResponseEntity<>(userOwner, HttpStatus.OK);
	}
	
	@PutMapping("/owner/{emailId}")
    public ResponseEntity<?> updateOwner(@PathVariable String emailId, @RequestBody UserOwner userOwner,@RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		emailId = emailId.replace("%40", "@");
		
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		userOwner.setEmailId(emailId);
        UserOwner updateUserOwner =userOwnerService.updateUserOwner(userOwner);
        return new ResponseEntity<>(updateUserOwner, HttpStatus.OK);
    }
	
	
	
	@DeleteMapping("/owner/{emailId}")
	public ResponseEntity<?> deleteUser(@PathVariable String emailId, @RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		boolean isDeleted = userOwnerService.deleteUserOwner(emailId);
		ResponseEntity<?> entity = new ResponseEntity<>("Email Id Not Exist", HttpStatus.NOT_FOUND);
		if (isDeleted) {
			entity = new ResponseEntity<>("owner with id: " + emailId + " deleted successfully", HttpStatus.OK);
		}
		return entity;
	}
	
	
	
	
	/*
	 * Below are the endpoints for accessing placeService
	 */
	
	@GetMapping("/owner/{ownerEmailId}/myPlaces")
    public ResponseEntity<?> getPlacesByOwnerId(@PathVariable("ownerEmailId") String ownerEmailId, @RequestHeader("Authorization") String token) {
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, ownerEmailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		ownerEmailId = ownerEmailId.replace("%40", "@");
		String url = "http://localhost:9093/api/easeMyBooking/places/owner/" + ownerEmailId;
        
        // Set the Authorization header with the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        
        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // Make the request and get the response
        ResponseEntity<Place[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Place[].class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Place> placeList = List.of(response.getBody());
            return ResponseEntity.ok(placeList);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Owner Id not found");
        }
    }
	
	@PostMapping("/owner/{ownerEmailId}/createPlace")
    public ResponseEntity<?> createPlace(@RequestBody Place place, @RequestHeader("Authorization") String token,@PathVariable("ownerEmailId") String ownerEmailId) {
		// "/{ownerEmailId}/place"
		String extractedToken = token.substring(7); 
		System.out.println(ownerEmailId + " " + extractedToken);
		if (!jwtUtil.validateToken(extractedToken, ownerEmailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
        String url = "http://localhost:9093/api/easeMyBooking/" + ownerEmailId + "/place";
        
     // Create HttpHeaders and set the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        // Create an HttpEntity with the headers and the place object
        HttpEntity<Place> entity = new HttpEntity<>(place, headers);
        
        try {
            ResponseEntity<Place> response = restTemplate.exchange(url, HttpMethod.POST, entity, Place.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to add place");
            }
        }catch (HttpClientErrorException e) {
            // Handle HTTP errors (e.g., 4xx and 5xx status codes)
        	return new ResponseEntity<>(place.getPlaceName()+" "+e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
	
	@PutMapping("/owner/{ownerEmailId}/updatePlace/{placeId}")
    public ResponseEntity<?> updatePlace(@PathVariable("placeId") int placeId, @RequestBody Place place, @RequestHeader("Authorization") String token,@PathVariable("ownerEmailId") String ownerEmailId) {
        
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, ownerEmailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		

		String url = "http://localhost:9093/api/easeMyBooking/" + ownerEmailId+"/place/"+placeId;

        // Create HttpHeaders and set the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        // Create an HttpEntity with the headers and the place object
        HttpEntity<Place> entity = new HttpEntity<>(place, headers);

        try {
            // Make the request and get the response
            ResponseEntity<Place> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Place.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to update place");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
	
	@DeleteMapping("/owner/{ownerEmailId}/deletePlace/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable("placeId") int placeId, @RequestHeader("Authorization") String token,@PathVariable("ownerEmailId") String ownerEmailId) {
       
		
		
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, ownerEmailId, "owner")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
    	ownerEmailId = ownerEmailId.replace("%40", "@");

    	
    	///{ownerEmailId}/place/{id}
    	String url = "http://localhost:9093/api/easeMyBooking/"+ownerEmailId+"/place/" + placeId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("Place deleted successfully");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to delete place");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
    
/*
 *  Methods for communicating with booking service
 */
	
	
	@GetMapping("/owner/{ownerEmailId}/userBookings")
	public ResponseEntity<?> getUserBookingsByOwner(@PathVariable("ownerEmailId") String ownerEmailId, @RequestHeader("Authorization") String token) {
    String extractedToken = token.substring(7); 
    if (!jwtUtil.validateToken(extractedToken, ownerEmailId, "owner")) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    System.out.println("got here");
    // Fetch all places for the owner
    String placesUrl = "http://localhost:9093/api/easeMyBooking/places/owner/" + ownerEmailId;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    ResponseEntity<Place[]> placesResponse;
    try {
        placesResponse = restTemplate.exchange(placesUrl, HttpMethod.GET, entity, Place[].class);
        if (!placesResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(placesResponse.getStatusCode()).body("Failed to retrieve places");
        }
    } catch (HttpClientErrorException | HttpServerErrorException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
    }
    
    System.out.println("got here tooooo");
    
    List<Place> places = List.of(placesResponse.getBody());
    List<UserBookingsResponse> allUserBookings = new ArrayList<>();

    // Fetch bookings for each place and retrieve user details
    for (Place place : places) {
        String bookingsUrl = "http://localhost:8079/bookings/"+ ownerEmailId+"/"+ place.getPlaceId();
        System.out.println("in loop");
        try {
            ResponseEntity<UserBookingsResponse> bookingsResponse = restTemplate.exchange(bookingsUrl, HttpMethod.GET, entity, UserBookingsResponse.class);
            if (bookingsResponse.getStatusCode().is2xxSuccessful()) {
                UserBookingsResponse bookingsResponseBody = bookingsResponse.getBody();
                if (bookingsResponseBody != null && bookingsResponseBody.getBookings() != null) {
                    List<Booking> bookings = bookingsResponseBody.getBookings();
                    allUserBookings.add(new UserBookingsResponse(place.getPlaceName(),place.getPlaceId(), bookings));
                    
                }
            } else {
                return ResponseEntity.status(bookingsResponse.getStatusCode()).body("Failed to retrieve bookings for place: " + place.getPlaceId());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    return ResponseEntity.ok(allUserBookings);
	}

	}
