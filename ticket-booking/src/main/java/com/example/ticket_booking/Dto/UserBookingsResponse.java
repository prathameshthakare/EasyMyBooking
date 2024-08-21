package com.example.ticket_booking.Dto;

import com.example.ticket_booking.model.Booking;
import java.util.List;

public class UserBookingsResponse {
    private String message;
    private List<Booking> bookings;

    public UserBookingsResponse(String message, List<Booking> bookings) {
        this.message = message;
        this.bookings = bookings;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
