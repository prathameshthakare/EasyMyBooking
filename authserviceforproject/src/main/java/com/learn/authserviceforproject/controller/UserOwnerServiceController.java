package com.learn.authserviceforproject.controller;

import java.util.List;

import com.learn.authserviceforproject.dto.UserBookingsResponse;
//import com.learn.authserviceforproject.context.UserContext;
import com.learn.authserviceforproject.model.Booking;
import com.learn.authserviceforproject.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import org.springframework.web.client.RestTemplate;

import com.learn.authserviceforproject.dto.BookingRequest;
import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
import com.learn.authserviceforproject.model.Place;
import com.learn.authserviceforproject.model.UserOwner;
import com.learn.authserviceforproject.service.UserOwnerService;
import com.learn.authserviceforproject.utility.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserOwnerServiceController {
	
	@Autowired
	private UserOwnerService userOwnerService;
	
	@Autowired
	private RestTemplate  restTemplate;

	@Autowired
	JwtUtil jwtUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerOwner(@RequestBody UserOwner userOwner) {
		ResponseEntity<?> entity;
		try {
			userOwnerService.registerUserOwner(userOwner);
			entity = new ResponseEntity<>("User Registered Successfully...", HttpStatus.CREATED);
		} catch (EmailIdAlreadyExistException e) {
			entity = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return entity;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> validateOwner(@RequestBody UserOwner userOwner){
		ResponseEntity<?> entity=null;
		if (userOwnerService.validateUserOwner(userOwner)) {
 		//	entity = new ResponseEntity<>("Owner Logged in Successfully...", HttpStatus.OK);
			System.out.println(userOwner.getEmailId() + "&&&&&" + userOwner.getRole());
			String token =jwtUtil.getToken(userOwner.getEmailId(), userOwner.getRole());
			entity = new ResponseEntity<>(token, HttpStatus.OK);
			} else {
			entity = new ResponseEntity<>("Invalid User Credentials...", HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<>(userOwnerService.getAllUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/user/{emailId}")
	public ResponseEntity<?> getUserById(@PathVariable String emailId,@RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "user")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		UserOwner userOwner = userOwnerService.getUserOwnerByEmailId(emailId);
		return new ResponseEntity<>(userOwner, HttpStatus.OK);
	}
	
	
	@PutMapping("/user/{emailId}")
	public ResponseEntity<?> updateUser(@PathVariable String emailId, @RequestBody UserOwner user, @RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "user")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		user.setEmailId(emailId);
		UserOwner updatedUser = userOwnerService.updateUserOwner(user);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/user/{emailId}")
	public ResponseEntity<?> deleteUser(@PathVariable String emailId, @RequestHeader("Authorization") String token) throws EmailIdNotExistException {
		String extractedToken = token.substring(7); 
		if (!jwtUtil.validateToken(extractedToken, emailId, "user")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		boolean isDeleted = userOwnerService.deleteUserOwner(emailId);
		ResponseEntity<?> entity = new ResponseEntity<>("Email Id Not Exist", HttpStatus.NOT_FOUND);
		if (isDeleted) {
			entity = new ResponseEntity<>("User with id: " + emailId + " deleted successfully", HttpStatus.OK);
		}
		return entity;
	}
	

	@GetMapping("user/{userEmailId}/showPlaces")
	public ResponseEntity<?> getAllPlaces(@RequestHeader("Authorization") String token,@PathVariable String userEmailId) {
        String url = "http://localhost:9093/api/easeMyBooking/user/"+userEmailId+"/place";
        
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
	
	/*
	 *  Methods for accessing booking service
	 */
	
	@PostMapping("/user/{userEmailId}/book")
    public ResponseEntity<?> createPlace(@RequestBody @Valid BookingRequest bookingRequest, @RequestHeader("Authorization") String token,@PathVariable("userEmailId") String userEmailId) {
		// "/{ownerEmailId}/place"
		String extractedToken = token.substring(7); 
		System.out.println(userEmailId + " " + extractedToken);
		if (!jwtUtil.validateToken(extractedToken, userEmailId, "user")) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
        String url = "http://localhost:8079/bookings/" + userEmailId + "/book";
        
     // Create HttpHeaders and set the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        // Create an HttpEntity with the headers and the place object
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, headers);
        
        try {
            ResponseEntity<BookingResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BookingResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to book place");
            }
        }catch (HttpClientErrorException e) {
            // Handle HTTP errors (e.g., 4xx and 5xx status codes)
        	return new ResponseEntity<>(bookingRequest.getPlaceId()+" "+e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
	
	// User fetching his bookings:
	

	@GetMapping("/user/{userEmailId}/myBookings")
	public ResponseEntity<?> getMyBookingsByUserEmailId(
	        @RequestHeader("Authorization") String token,
	        @PathVariable String userEmailId) {

	    String url = "http://localhost:8079/bookings/user/" + userEmailId + "/myBookings";

	    // Set the Authorization header with the JWT token
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", token);

	    // Create an HttpEntity with the headers
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // Make the request and get the response

	try {
		ResponseEntity<UserBookingsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserBookingsResponse.class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	    	UserBookingsResponse bookingsResponse = response.getBody();

	        if (bookingsResponse != null && bookingsResponse.getBookings() != null) {
	            List<Booking> bookingList = bookingsResponse.getBookings();
	            return ResponseEntity.ok(bookingList);
	        } else {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No bookings found");
	        }
	    } else {
	        return ResponseEntity.status(response.getStatusCode()).body("User Id not found");
	    }
	}
	catch (HttpClientErrorException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    } catch (Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	}
	
	@DeleteMapping("user/{userEmailId}/cancelBooking/{bookingId}")
	public ResponseEntity<?> cancelMyBookingsByBookingId(@RequestHeader("Authorization") String token, @PathVariable("userEmailId") String userEmailId,  @PathVariable("bookingId")String bookingId){
		String url = "http://localhost:8079/bookings/" + userEmailId + "/" + bookingId;

	    // Set the Authorization header with the JWT token
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", token);

	    // Create an HttpEntity with the headers
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
	    
	    return ResponseEntity.ok(response.getBody());

	}


}
