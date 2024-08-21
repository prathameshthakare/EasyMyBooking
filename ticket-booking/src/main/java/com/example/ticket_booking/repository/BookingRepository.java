package com.example.ticket_booking.repository;



import com.example.ticket_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by user ID
    List<Booking> findByUserEmailId(String userEmailId);
    List<Booking> findByPlaceId(int placeId);
}
