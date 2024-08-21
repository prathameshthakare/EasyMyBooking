package com.example.ticket_booking.controller;

import com.example.ticket_booking.Dto.BookingRequest;
import com.example.ticket_booking.Dto.BookingResponse;
import com.example.ticket_booking.Dto.UserBookingsResponse;
import com.example.ticket_booking.model.Booking;
import com.example.ticket_booking.service.BookingService;
import com.example.ticket_booking.context.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    
    
    @GetMapping("/{ownerEmailId}/{placeId}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable("ownerEmailId") String ownerEmailId,@PathVariable("placeId") int placeId,HttpServletRequest request) {
    	UserContext userContext = getUserContext(request);
        if (userContext == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserContext not found in request.");
        }
    	ResponseEntity<?> authorizationResponse = checkAuthorization(userContext, ownerEmailId, "owner");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }
        
    	System.out.println(userContext); 
        List<Booking> bookings = bookingService.getBookingsByPlaceId(placeId);
        UserBookingsResponse response = new UserBookingsResponse("Bookings retrieved successfully", bookings);
        return ResponseEntity.ok(response);
    }
    
    
    
    @PostMapping("/{userEmailId}/book")
    public ResponseEntity<?> createBooking(@PathVariable String userEmailId, @RequestBody @Valid BookingRequest bookingRequest,HttpServletRequest request) {
    	
    	UserContext userContext = getUserContext(request);
    	ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "user");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }
        LocalDate visitDate;
        try {
            visitDate = LocalDate.parse(bookingRequest.getVisitDate());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid visit date format. Use 'yyyy-MM-dd'.");
        }

        // Automatically fetch the current date and time for bookingDate
        LocalDateTime bookingDateTime = LocalDateTime.now();

        Booking booking = new Booking();
        booking.setUserEmailId(userEmailId);
        booking.setPlaceId(bookingRequest.getPlaceId());
        booking.setBookingDate(bookingDateTime);
        booking.setVisitDate(visitDate);
        booking.setStatus("Booked");

        try {
            Booking savedBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(new BookingResponse("Booking created successfully", savedBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating booking.");
        }
    }


    @GetMapping("/user/{userEmailId}/myBookings")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable String userEmailId,HttpServletRequest request) {
    	UserContext userContext = getUserContext(request);
    	
    	ResponseEntity<?> authorizationResponse = checkAuthorization(userContext, userEmailId, "user");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }
        
    	System.out.println(userContext); 
        List<Booking> bookings = bookingService.getBookingsByUserEmailId(userEmailId);
        UserBookingsResponse response = new UserBookingsResponse("Bookings retrieved successfully", bookings);
        return ResponseEntity.ok(response);
    }


    // Cancel a booking
    @DeleteMapping("/{userEmailId}/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable String userEmailId, @PathVariable Long bookingId, HttpServletRequest request) {
UserContext userContext = getUserContext(request);
    	
    	ResponseEntity<?> authorizationResponse = checkAuthorization(userContext, userEmailId, "user");
        if (authorizationResponse != null) {
            return authorizationResponse;
        }
        System.out.println("here it come");
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking deleted successfully");
    }
    
    
    /*
     *  Utility methods
     */
    
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

}