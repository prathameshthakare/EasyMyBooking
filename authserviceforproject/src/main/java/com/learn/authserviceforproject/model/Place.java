package com.learn.authserviceforproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import jakarta.persistence.Column;
import lombok.Data;

import org.springframework.data.mongodb.core.index.Indexed;

@Data

public class Place {
	@Transient
    public static final String SEQUENCE_NAME = "place_sequence";

    @Id
    private String id; // MongoDB _id field

    private int placeId; // Auto-incremented field
    private String ownerEmailId;
    
    @Indexed(unique = true)
    private String placeName;
    private String description;
    private int ticketPrice;
    
    
	public Place(String id, int placeId, String ownerEmailId, String placeName, String description, int ticketPrice) {
		super();
		this.id = id;
		this.placeId = placeId;
		this.ownerEmailId = ownerEmailId;
		this.placeName = placeName;
		this.description = description;
		this.ticketPrice = ticketPrice;
	}
	public Place() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public String getOwnerEmailId() {
		return ownerEmailId;
	}
	public void setOwnerEmailId(String ownerEmailId) {
		this.ownerEmailId = ownerEmailId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}
    
    
}
