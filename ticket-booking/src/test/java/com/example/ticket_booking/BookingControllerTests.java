package com.example.ticket_booking;

import com.example.ticket_booking.Dto.BookingRequest;
import com.example.ticket_booking.Dto.BookingResponse;
import com.example.ticket_booking.controller.BookingController;
import com.example.ticket_booking.model.Booking;
import com.example.ticket_booking.Dto.UserBookingsResponse;
import com.example.ticket_booking.service.BookingService;
import com.example.ticket_booking.context.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingControllerTests {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBookingsByUserId_Unauthorized() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("different@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        ResponseEntity<?> response = bookingController.getBookingsByUserId("owner@example.com", 1, request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Not Authorized", response.getBody());
    }

    @Test
    public void testGetBookingsByUserId_Authorized() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("owner@example.com");
        userContext.setRole("owner");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        List<Booking> bookings = new ArrayList<>();
        when(bookingService.getBookingsByPlaceId(1)).thenReturn(bookings);

        ResponseEntity<?> response = bookingController.getBookingsByUserId("owner@example.com", 1, request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserBookingsResponse);
    }


    @Test
    public void testCreateBooking_Unauthorized() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("different@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setPlaceId(1L);
        bookingRequest.setVisitDate("2024-08-10");

        ResponseEntity<?> response = bookingController.createBooking("owner@example.com", bookingRequest, request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Not Authorized", response.getBody());
    }

    @Test
    public void testCreateBooking_Success() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("owner@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setPlaceId(1L);
        bookingRequest.setVisitDate("2024-08-10");

        Booking booking = new Booking();
        booking.setUserEmailId("owner@example.com");
        booking.setPlaceId(1L);
        booking.setBookingDate(LocalDateTime.now());
        booking.setVisitDate(LocalDate.parse("2024-08-10"));
        booking.setStatus("Booked");

        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        ResponseEntity<?> response = bookingController.createBooking("owner@example.com", bookingRequest, request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof BookingResponse);
    }


    @Test
    public void testGetBookingsByUserId_Success() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("owner@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        List<Booking> bookings = new ArrayList<>();
        when(bookingService.getBookingsByUserEmailId("owner@example.com")).thenReturn(bookings);

        ResponseEntity<?> response = bookingController.getBookingsByUserId("owner@example.com", request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserBookingsResponse);
    }

    @Test
    public void testCancelBooking_Unauthorized() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("different@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        ResponseEntity<?> response = bookingController.cancelBooking("owner@example.com", 1L, request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Not Authorized", response.getBody());
    }

    @Test
    public void testCancelBooking_Success() {
        UserContext userContext = new UserContext();
        userContext.setEmailId("owner@example.com");
        userContext.setRole("user");

        when(request.getAttribute("userContext")).thenReturn(userContext);

        doNothing().when(bookingService).cancelBooking(1L);

        ResponseEntity<?> response = bookingController.cancelBooking("owner@example.com", 1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Booking deleted successfully", response.getBody());
    }
}
