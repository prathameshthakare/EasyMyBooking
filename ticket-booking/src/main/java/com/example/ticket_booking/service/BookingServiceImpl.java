package com.example.ticket_booking.service;



import com.example.ticket_booking.model.Booking;
import com.example.ticket_booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByUserEmailId(String userEmailId) {
        return bookingRepository.findByUserEmailId(userEmailId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        // Optionally check if booking exists before deleting
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            bookingRepository.deleteById(bookingId);
        } else {
            // Handle the case where the booking is not found, if necessary
            throw new RuntimeException("Booking not found with id " + bookingId);
        }

    }
    
    @Override
    public List<Booking> getBookingsByPlaceId(int placeId) {
        return bookingRepository.findByPlaceId(placeId);
    }

}
