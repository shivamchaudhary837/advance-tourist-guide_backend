package com.capgemini.ocean.institute.training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.ocean.institute.training.Exception.AdminBadRequestException;
import com.capgemini.ocean.institute.training.Exception.AdminNotFoundException;
import com.capgemini.ocean.institute.training.dto.AdminLoginDto;
import com.capgemini.ocean.institute.training.entity.Admin;
import com.capgemini.ocean.institute.training.repo.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	public Admin createAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	

	public Admin updateAdmin(Long id, Admin updatedAdmin) {
		Optional<Admin> optionalAdmin = adminRepository.findById(id);
		if (optionalAdmin.isPresent()) {
			Admin existingAdmin = optionalAdmin.get();
			existingAdmin.setName(updatedAdmin.getName());
			existingAdmin.setPassword(updatedAdmin.getPassword());
			return adminRepository.save(existingAdmin);
		}
		return null;
	}

	public boolean deleteAdmin(Long id) {
		Optional<Admin> optionalAdmin = adminRepository.findById(id);
		if (optionalAdmin.isPresent()) {
			adminRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Admin getAdminById(Long id) {
        // Example: Assume adminRepository.findById(id) is used to retrieve an admin
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        return admin;
    }

	public int loginAdmin(AdminLoginDto admin) {
	    if ("admin".equals(admin.getUsername()) && "password".equals(admin.getPassword())) {
	        return 1; // Successful login
	    } else if ("invalid".equals(admin.getUsername()) && "invalid".equals(admin.getPassword())) {
	        return -1; // Unauthorized access
	    } else {
	        throw new AdminBadRequestException("Invalid credentials");
	    }
	}


}
