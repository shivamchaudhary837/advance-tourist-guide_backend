package com.capgemini.ocean.institute.training.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.ocean.institute.training.entity.Place;
import com.capgemini.ocean.institute.training.repo.PlaceRepository;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place getPlaceById(Long placeID) {
        return placeRepository.findById(placeID).orElse(null);
    }

    public Place addPlace(Place place) {
        return placeRepository.save(place);
    }

    public Place updatePlace(Long placeID, Place place) {
        Optional<Place> existingPlaceOptional = placeRepository.findById(placeID);

        if (existingPlaceOptional.isPresent()) {
            Place existingPlace = existingPlaceOptional.get();
            existingPlace.setName(place.getName());
            existingPlace.setAddress(place.getAddress());
            existingPlace.setArea(place.getArea());
            existingPlace.setDistance(place.getDistance());
            existingPlace.setDescription(place.getDescription());
            return placeRepository.save(existingPlace);
        } else {
            return null; // Handle not found scenario
        }
    }

    public void deletePlace(Long placeID) {
        placeRepository.deleteById(placeID);
    }
    
    public List<String> getAllTags() {
        List<Place> allPlaces = placeRepository.findAll();
        return allPlaces.stream()
                .map(Place::getTag)
                .distinct()
                .collect(Collectors.toList());
    }
    
   
    public List<Place> getPlacesByTag(String tag) {
        return placeRepository.findByTag(tag);
    }

    
}
