package com.learn.easeMyBooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.learn.easeMyBooking.model.Place;

@Repository
public interface PlaceRepository extends MongoRepository<Place, String> {
    Optional<Place> findByPlaceId(int placeId);
    Optional<Place> findByPlaceName(String placeName);
    List<Place> findPlacesByOwnerEmailId(String ownerEmailId);
    Place findByIdAndOwnerEmailId(int placeId, String ownerEmailId);
    
}
