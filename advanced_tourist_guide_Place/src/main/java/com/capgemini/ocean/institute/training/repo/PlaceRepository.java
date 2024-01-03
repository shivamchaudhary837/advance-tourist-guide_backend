package com.capgemini.ocean.institute.training.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.ocean.institute.training.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Long>{

	List<Place> save(String tag);

	List<Place> findByTag(String tag);

	
}
