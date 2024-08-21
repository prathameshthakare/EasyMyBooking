package com.example.ticket_booking.Dto;

import com.example.ticket_booking.model.Booking;

public class BookingResponse {
    private String message;
    private Booking booking;

    public BookingResponse(String message, Booking booking) {
        this.message = message;
        this.booking = booking;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
