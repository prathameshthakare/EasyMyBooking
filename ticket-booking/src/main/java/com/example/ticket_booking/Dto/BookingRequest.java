package com.example.ticket_booking.Dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BookingRequest {



    @NotNull(message = "User ID is required")
    private String userEmailId;

    @NotNull(message = "Place ID is required")
    private Long placeId;

    @NotNull(message = "Booking date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "Invalid date format. Use 'yyyy-MM-dd'T'HH:mm:ss'")
    private String bookingDate;
    private String visitDate;

    // Getters and Setters

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}
