package com.capgemini.ocean.institute.training.controller;

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

import com.capgemini.ocean.institute.training.Exception.AdminBadRequestException;
import com.capgemini.ocean.institute.training.Exception.AdminNotFoundException;
import com.capgemini.ocean.institute.training.Exception.AdminUnauthorizedException;
import com.capgemini.ocean.institute.training.dto.AdminLoginDto;
import com.capgemini.ocean.institute.training.entity.Admin;
import com.capgemini.ocean.institute.training.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/{id}")
	public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
		try {
			Admin admin = adminService.getAdminById(id);
			return new ResponseEntity<>(admin, HttpStatus.OK);
		} catch (AdminNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
		try {
			Admin updatedAdmin = adminService.updateAdmin(id, admin);
			return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
		} catch (AdminNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
		try {
			boolean success = adminService.deleteAdmin(id);
			if (success) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (AdminNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginAdmin(@RequestBody AdminLoginDto admin) {
	    try {
	        int result = adminService.loginAdmin(admin);

	        if (result == 0) {
	            throw new AdminBadRequestException("Invalid request");
	        }
	        if (result == -1) {
	            throw new AdminUnauthorizedException("Unauthorized access");
	        }

	        return new ResponseEntity<>("Login successful", HttpStatus.OK);
	    } catch (AdminBadRequestException e) {
	        return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
	    } catch (AdminUnauthorizedException e) {
	        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
	    }
	}

}
