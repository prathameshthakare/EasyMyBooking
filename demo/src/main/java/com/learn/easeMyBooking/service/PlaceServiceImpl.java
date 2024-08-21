package com.learn.easeMyBooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.easeMyBooking.Exception.DuplicatePlaceNameException;
import com.learn.easeMyBooking.model.Place;
import com.learn.easeMyBooking.repository.PlaceRepository;
import com.mongodb.DuplicateKeyException;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Place addPlace(Place place) {
    	Optional<Place> optional = placeRepository.findByPlaceName(place.getPlaceName());
    	 if (optional.isPresent()) {
    		 throw new DuplicatePlaceNameException("Place name already exists");
         }
        place.setPlaceId(sequenceGeneratorService.getNextSequence(Place.SEQUENCE_NAME));
        try {
            return placeRepository.save(place);
        } catch (DuplicateKeyException e) {
            throw new DuplicatePlaceNameException("Place name already exists");
        }
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Place getPlaceById(int placeId) {
        return placeRepository.findByPlaceId(placeId).orElse(null);
    }


    @Override
    public boolean deletePlace(int placeId) {
        Optional<Place> optional = placeRepository.findByPlaceId(placeId);
        boolean isDeleted = false;
        if (optional.isPresent()) {
            placeRepository.deleteById(optional.get().getId());
            isDeleted = true;
        }
        return isDeleted;
    }
    
    
    
    
    @Override
    public Place updatePlace(int placeId, Place place) {
        Optional<Place> existingPlace = placeRepository.findByPlaceId(placeId);
        if (existingPlace.isPresent()) {
            Place updatedPlace = existingPlace.get();
            updatedPlace.setOwnerEmailId(place.getOwnerEmailId());
            updatedPlace.setPlaceName(place.getPlaceName());
            updatedPlace.setDescription(place.getDescription());
            updatedPlace.setTicketPrice(place.getTicketPrice());
            return placeRepository.save(updatedPlace);
        }
        return null;
    }

	@Override
	public List<Place> getPlacesByOwnerEmailId(String emailId) {
		 return  placeRepository.findPlacesByOwnerEmailId(emailId);
	}
}
