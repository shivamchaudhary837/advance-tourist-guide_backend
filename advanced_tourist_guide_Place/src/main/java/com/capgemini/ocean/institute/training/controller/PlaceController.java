package com.capgemini.ocean.institute.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.ocean.institute.training.entity.Place;
import com.capgemini.ocean.institute.training.exception.PlaceBadRequestException;
import com.capgemini.ocean.institute.training.exception.PlaceNotFoundException;
import com.capgemini.ocean.institute.training.service.PlaceService;

@RestController
@RequestMapping("/place")
@CrossOrigin("*")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/viewplace")
    public ResponseEntity<List<Place>> getAllPlaces() {
        try {
            List<Place> places = placeService.getAllPlaces();
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{placeID}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long placeID) {
        try {
            Place place = placeService.getPlaceById(placeID);
            return new ResponseEntity<>(place, HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Place> addPlace(@RequestBody Place place) {
        try {
            Place addedPlace = placeService.addPlace(place);
            return new ResponseEntity<>(addedPlace, HttpStatus.CREATED);
        } catch (PlaceBadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{placeID}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long placeID, @RequestBody Place place) {
        try {
            Place updatedPlace = placeService.updatePlace(placeID, place);
            return new ResponseEntity<>(updatedPlace, HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{placeID}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long placeID) {
        try {
            placeService.deletePlace(placeID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllTags() {
        List<String> tags = placeService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Place>> getPlacesByTag(@PathVariable String tag) {
        try {
            List<Place> places = placeService.getPlacesByTag(tag);
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
