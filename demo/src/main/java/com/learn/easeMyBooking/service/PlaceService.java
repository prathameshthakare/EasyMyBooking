package com.learn.easeMyBooking.service;

import java.util.List;

import com.learn.easeMyBooking.model.Place;


public interface PlaceService {
	//public Cart addCart(Cart cart);	
//	public List<Cart> getAllCart();	
//	public Cart  getCartById(int cartId);
//	public Cart getCartByEmailId(String emailId);
//	public  boolean deleteCart(int cartId);
	
	public Place addPlace(Place place);
	public List<Place> getAllPlaces();
	public Place getPlaceById(int placeId);
	
	public boolean deletePlace(int placeId);
	public Place updatePlace(int placeId, Place place);
	public List<Place> getPlacesByOwnerEmailId(String emailId); 

}
