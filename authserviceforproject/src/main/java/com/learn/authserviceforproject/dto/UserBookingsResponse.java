package com.learn.authserviceforproject.dto;

import com.learn.authserviceforproject.model.Booking;
import java.util.List;

public class UserBookingsResponse {
    private String placeName;
    private long placeId;
    private List<Booking> bookings;
	public UserBookingsResponse(String placeName, long placeId, List<Booking> bookings) {
		super();
		this.placeName = placeName;
		this.placeId = placeId;
		this.bookings = bookings;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public long getPlaceId() {
		return placeId;
	}
	public void setPlaceId(long placeId) {
		this.placeId = placeId;
	}
	public List<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
    
    
}
