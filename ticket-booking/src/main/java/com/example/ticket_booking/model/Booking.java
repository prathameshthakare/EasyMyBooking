package com.example.ticket_booking.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmailId;

    @Column(nullable = false)
    private Long placeId;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "visit_date")
    private LocalDate visitDate;


    @Column(nullable = false)
    private String status;

    public Booking(Long id, String userEmailId, Long placeId, LocalDateTime bookingDate, LocalDate visitDate, String status) {
        this.id = id;
        this.userEmailId = userEmailId;
        this.placeId = placeId;
        this.bookingDate = bookingDate;
        this.visitDate = visitDate;
        this.status = status;
    }

    public Booking() {

    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userEmailId +
                ", placeId=" + placeId +
                ", bookingDate=" + bookingDate +
                ", visitDate=" + visitDate +
                ", status='" + status + '\'' +
                '}';
    }
}
