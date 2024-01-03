package com.capgemini.ocean.institute.training.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.ocean.institute.training.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

	

	Admin findByEmail(String username);

}
