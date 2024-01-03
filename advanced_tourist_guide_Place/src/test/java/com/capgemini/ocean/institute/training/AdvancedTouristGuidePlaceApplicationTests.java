package com.capgemini.ocean.institute.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.capgemini.ocean.institute.training.entity.Place;
import com.capgemini.ocean.institute.training.repo.PlaceRepository;
import com.capgemini.ocean.institute.training.service.PlaceService;

@SpringBootTest
public class AdvancedTouristGuidePlaceApplicationTests {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPlaces() {
        // Arrange
        List<Place> places = new ArrayList<>();
        when(placeRepository.findAll()).thenReturn(places);

        // Act
        List<Place> result = placeService.getAllPlaces();

        // Assert
        assertEquals(places, result);
    }

    @Test
    void getPlaceById() {
        // Arrange
        Long placeId = 1L;
        Place place = new Place();
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // Act
        Place result = placeService.getPlaceById(placeId);

        // Assert
        assertEquals(place, result);
    }

    @Test
    void addPlace() {
        // Arrange
        Place place = new Place();
        when(placeRepository.save(place)).thenReturn(place);

        // Act
        Place result = placeService.addPlace(place);

        // Assert
        assertEquals(place, result);
    }

    @Test
    void updatePlace() {
        // Arrange
        Long placeId = 1L;
        Place existingPlace = new Place();
        existingPlace.setPlaceID(placeId);
        existingPlace.setName("Old Name");

        Place updatedPlace = new Place();
        updatedPlace.setPlaceID(placeId);
        updatedPlace.setName("New Name");

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(existingPlace));
        when(placeRepository.save(existingPlace)).thenReturn(updatedPlace);

        // Act
        Place result = placeService.updatePlace(placeId, updatedPlace);

        // Assert
        assertEquals(updatedPlace, result);
        assertEquals("New Name", result.getName());
    }

    @Test
    void deletePlace() {
        // Arrange
        Long placeId = 1L;

        // Act
        placeService.deletePlace(placeId);

        // Assert
        verify(placeRepository, times(1)).deleteById(placeId);
    }
    
    

        @Test
        void getAllPlaces_shouldReturnListOfPlaces() {
            // Arrange
            List<Place> places = new ArrayList<>();
            when(placeRepository.findAll()).thenReturn(places);

            // Act
            List<Place> result = placeService.getAllPlaces();

            // Assert
            assertEquals(places, result);
        }

        @Test
        void getPlaceById_shouldReturnPlace() {
            // Arrange
            Long placeId = 1L;
            Place expectedPlace = new Place();
            when(placeRepository.findById(placeId)).thenReturn(Optional.of(expectedPlace));

            // Act
            Place result = placeService.getPlaceById(placeId);

            // Assert
            assertEquals(expectedPlace, result);
        }

        @Test
        void addPlace_shouldReturnAddedPlace() {
            // Arrange
            Place placeToAdd = new Place();
            when(placeRepository.save(any(Place.class))).thenReturn(placeToAdd);

            // Act
            Place result = placeService.addPlace(placeToAdd);

            // Assert
            assertEquals(placeToAdd, result);
        }

        @Test
        void updatePlace_shouldReturnUpdatedPlace() {
            // Arrange
            Long placeId = 1L;
            Place placeToUpdate = new Place();
            Optional<Place> existingPlaceOptional = Optional.of(new Place());
            when(placeRepository.findById(placeId)).thenReturn(existingPlaceOptional);
            when(placeRepository.save(any(Place.class))).thenReturn(placeToUpdate);

            // Act
            Place result = placeService.updatePlace(placeId, placeToUpdate);

            // Assert
            assertEquals(placeToUpdate, result);
        }

        @Test
        void deletePlace_shouldInvokeRepositoryDelete() {
            // Arrange
            Long placeId = 1L;
            doNothing().when(placeRepository).deleteById(placeId);

            // Act
            placeService.deletePlace(placeId);

            // Assert
            // Verify that deleteById was called once with the correct argument
            verify(placeRepository, times(1)).deleteById(placeId);
        }
        
        @Test
        void getAllTags_shouldReturnListOfTags() {
            // Arrange
            List<Place> places = new ArrayList<>();
            places.add(new Place(1L, "Place1", "Address1", "Area1", 10.0, "Description1", "Tag1", null));
            places.add(new Place(2L, "Place2", "Address2", "Area2", 15.0, "Description2", "Tag2", null));
            when(placeRepository.findAll()).thenReturn(places);

            // Act
            List<String> result = placeService.getAllTags();

            // Assert
            assertEquals(List.of("Tag1", "Tag2"), result);
        }
        @Test
        void getPlacesByTag_shouldReturnPlacesWithGivenTag() {
            // Arrange
            String tag = "Tag1";
            List<Place> placesWithTag = List.of(
                    new Place(1L, "Place1", "Address1", "Area1", 10.0, "Description1", tag, null),
                    new Place(2L, "Place2", "Address2", "Area2", 15.0, "Description2", tag, null)
            );
            when(placeRepository.findByTag(tag)).thenReturn(placesWithTag);

            // Act
            List<Place> result = placeService.getPlacesByTag(tag);

            // Assert
            assertEquals(placesWithTag, result);
        }

        @Test
        void getPlacesByTag_shouldReturnEmptyListForNonexistentTag() {
            // Arrange
            String nonExistingTag = "NonExistingTag";
            when(placeRepository.findByTag(nonExistingTag)).thenReturn(Collections.emptyList());

            // Act
            List<Place> result = placeService.getPlacesByTag(nonExistingTag);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        void getPlacesByTag_shouldInvokeRepositoryMethod() {
            // Arrange
            String tag = "Tag1";

            // Act
            placeService.getPlacesByTag(tag);

            // Assert
            // Verify that findByTag was called once with the correct argument
            verify(placeRepository, times(1)).findByTag(tag);
        }
       
        

       
        @Test
        void getAllTags_shouldReturnDistinctListOfTags() {
            // Arrange
            List<Place> places = new ArrayList<>();
            places.add(new Place(1L, "Place1", "Address1", "Area1", 10.0, "Description1", "Tag1", null));
            places.add(new Place(2L, "Place2", "Address2", "Area2", 15.0, "Description2", "Tag2", null));
            places.add(new Place(3L, "Place3", "Address3", "Area3", 20.0, "Description3", "Tag1", null));

            when(placeRepository.findAll()).thenReturn(places);

            // Act
            List<String> result = placeService.getAllTags();

            // Assert
            assertEquals(List.of("Tag1", "Tag2"), result);
        }

       

       

        

       
 }


