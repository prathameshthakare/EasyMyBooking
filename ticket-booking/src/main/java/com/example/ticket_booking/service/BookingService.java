package com.example.ticket_booking.service;


import com.example.ticket_booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByUserEmailId(String userEmailId);
    List<Booking> getBookingsByPlaceId(int placeId);
    

    void cancelBooking(Long bookingId);
}
