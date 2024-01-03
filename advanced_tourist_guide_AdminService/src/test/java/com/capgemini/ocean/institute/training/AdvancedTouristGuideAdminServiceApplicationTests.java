package com.capgemini.ocean.institute.training;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.ocean.institute.training.dto.AdminLoginDto;
import com.capgemini.ocean.institute.training.entity.Admin;
import com.capgemini.ocean.institute.training.repo.AdminRepository;
import com.capgemini.ocean.institute.training.service.AdminService;


public class AdvancedTouristGuideAdminServiceApplicationTests {

	@Mock
	private AdminRepository adminRepository;

	@InjectMocks
	private AdminService adminService;

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllAdmins() {
		// Arrange
		List<Admin> admins = Arrays.asList(new Admin(1L, "Manish", "password", null),
				new Admin(2L, "Shivam", "password", null));
		when(adminRepository.findAll()).thenReturn(admins);

		// Act
		List<Admin> result = adminService.getAllAdmins();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Manish", result.get(0).getName());
		assertEquals("Shivam", result.get(1).getName());
		verify(adminRepository, times(1)).findAll();
	}

	@Test
	public void testCreateAdmin() {
		// Arrange
		Admin admin = new Admin(1L, "Manish", "password", null);
		when(adminRepository.save(admin)).thenReturn(admin);

		// Act
		Admin result = adminService.createAdmin(admin);

		// Assert
		assertNotNull(result);
		assertEquals("Manish", result.getName());
		verify(adminRepository, times(1)).save(admin);
	}

	@Test
	public void testGetAdminById() {
		// Arrange
		Long adminId = 1L;
		Admin admin = new Admin(adminId, "Manish", "password", null);
		when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

		// Act
		Admin result = adminService.getAdminById(adminId);

		// Assert
		assertNotNull(result);
		assertEquals("Manish", result.getName());
		verify(adminRepository, times(1)).findById(adminId);
	}

	@Test
	public void testUpdateAdmin() {
		// Arrange
		Long adminId = 1L;
		Admin existingAdmin = new Admin(adminId, "Manish", "password", null);
		Admin updatedAdmin = new Admin(adminId, "UpdatedName", "newPassword", null);

		when(adminRepository.findById(adminId)).thenReturn(Optional.of(existingAdmin));
		when(adminRepository.save(existingAdmin)).thenReturn(existingAdmin);

		// Act
		Admin result = adminService.updateAdmin(adminId, updatedAdmin);

		// Assert
		assertNotNull(result);
		assertEquals("UpdatedName", result.getName());
		verify(adminRepository, times(1)).findById(adminId);
		verify(adminRepository, times(1)).save(existingAdmin);
	}

	@Test
	public void testDeleteAdmin() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.of(new Admin()));

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertTrue(result);
		verify(adminRepository, times(1)).deleteById(adminId);
	}

	@Test
	public void testDeleteAdminWhenNotFound() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertFalse(result);
		verify(adminRepository, never()).deleteById(adminId);
	}

	@Test
	public void testGetAllAdminsWhenEmpty() {
		// Arrange
		when(adminRepository.findAll()).thenReturn(Collections.emptyList());

		// Act
		List<Admin> result = adminService.getAllAdmins();

		// Assert
		assertTrue(result.isEmpty());
		verify(adminRepository, times(1)).findAll();
	}

	@Test
	public void testUpdateAdminWhenNotFound() {
		// Arrange
		Long adminId = 1L;
		Admin updatedAdmin = new Admin(adminId, "UpdatedName", "newPassword", null);

		when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

		// Act
		Admin result = adminService.updateAdmin(adminId, updatedAdmin);

		// Assert
		assertNull(result);
		verify(adminRepository, never()).save(any());
	}

	@Test
	public void testUpdateAdminWithNullInput() {
		// Act
		Admin result = adminService.updateAdmin(1L, null);

		// Assert
		assertNull(result);
		verify(adminRepository, never()).save(any());
	}

	@Test
	public void testGetAllAdminsException() {
		// Arrange
		when(adminRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.getAllAdmins());
		verify(adminRepository, times(1)).findAll();
	}

	@Test
	public void testCreateAdminException() {
		// Arrange
		when(adminRepository.save(any(Admin.class))).thenThrow(new RuntimeException("Simulated exception"));

		// Act and Assert
		Admin admin = new Admin(3L, "John", "password", null);
		assertThrows(RuntimeException.class, () -> adminService.createAdmin(admin));
		verify(adminRepository, times(1)).save(any(Admin.class));
	}

	@Test
	public void testGetAdminByIdException() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenThrow(new RuntimeException("Simulated exception"));

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.getAdminById(adminId));
		verify(adminRepository, times(1)).findById(adminId);
	}

	@Test
	public void testUpdateAdminException() {
		// Arrange
		Long adminId = 1L;
		Admin updatedAdmin = new Admin(adminId, "UpdatedName", "newPassword", null);

		when(adminRepository.findById(adminId)).thenReturn(Optional.of(new Admin()));
		when(adminRepository.save(any(Admin.class))).thenThrow(new RuntimeException("Simulated exception"));

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.updateAdmin(adminId, updatedAdmin));
		verify(adminRepository, times(1)).findById(adminId);
		verify(adminRepository, times(1)).save(any(Admin.class));
	}

	@Test
	public void testDeleteAdminException() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.of(new Admin()));
		doThrow(new RuntimeException("Simulated exception")).when(adminRepository).deleteById(adminId);

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.deleteAdmin(adminId));
		verify(adminRepository, times(1)).deleteById(adminId);
	}

	@Test
	public void testGetAllAdminsEmptyList() {
		// Arrange
		when(adminRepository.findAll()).thenReturn(Collections.emptyList());

		// Act
		List<Admin> result = adminService.getAllAdmins();

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(adminRepository, times(1)).findAll();
	}

	@Test
	public void testUpdateAdminNotFound() {
		// Arrange
		Long adminId = 1L;
		Admin updatedAdmin = new Admin(adminId, "UpdatedName", "newPassword", null);

		when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

		// Act
		Admin result = adminService.updateAdmin(adminId, updatedAdmin);

		// Assert
		assertNull(result);
		verify(adminRepository, times(1)).findById(adminId);
		verify(adminRepository, never()).save(any(Admin.class));
	}

	@Test
	public void testDeleteAdminNotFound() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertFalse(result);
		verify(adminRepository, never()).deleteById(adminId);
	}

	@Test
	public void testDeleteAdminExceptionOnDelete() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.of(new Admin()));
		doThrow(new RuntimeException("Simulated exception")).when(adminRepository).deleteById(adminId);

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.deleteAdmin(adminId));
		verify(adminRepository, times(1)).deleteById(adminId);
	}

	@Test
	public void testDeleteAdminExceptionOnFindById() {
		// Arrange
		Long adminId = 1L;
		when(adminRepository.findById(adminId)).thenThrow(new RuntimeException("Simulated exception"));

		// Act and Assert
		assertThrows(RuntimeException.class, () -> adminService.deleteAdmin(adminId));
		verify(adminRepository, times(1)).findById(adminId);
		verify(adminRepository, never()).deleteById(adminId);
	}

//    @Test
//    public void testLoginAdminNotFound() {
//        // Arrange
//        AdminLoginDto adminLoginDto = new AdminLoginDto();
//        when(adminRepository.findByEmail(adminLoginDto.getUsername())).thenReturn(null);
//
//        // Act
//        int result = adminService.loginAdmin(adminLoginDto);
//
//        // Assert
//        assertEquals(0, result);
//        verify(adminRepository, times(1)).findByEmail(adminLoginDto.getUsername());
//    }

//    @Test
//    public void testLoginAdminIncorrectPassword() {
//        // Arrange
//        Admin savedAdmin = new Admin(1L, "testadmin", "password", null);
//        AdminLoginDto adminLoginDto = new AdminLoginDto();
//        when(adminRepository.findByEmail(adminLoginDto.getUsername())).thenReturn(savedAdmin);
//
//        // Act
//        int result = adminService.loginAdmin(adminLoginDto);
//
//        // Assert
//        assertEquals(-1, result);
//        verify(adminRepository, times(1)).findByEmail(adminLoginDto.getUsername());
//    }

	@Test
	public void testCreateAdminNullInput() {
		// Act
		Admin result = adminService.createAdmin(null);

		// Assert
		assertNull(result);
		verify(adminRepository, never()).save(any(Admin.class));
	}

	@Test
	public void testUpdateAdminNullInput() {
		// Act
		Admin result = adminService.updateAdmin(1L, null);

		// Assert
		assertNull(result);
		verify(adminRepository, never()).save(any(Admin.class));
	}

	@Test
	public void testDeleteAdminNonexistentId() {
		// Arrange
		Long adminId = 999L;
		when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertFalse(result);
		verify(adminRepository, never()).deleteById(adminId);
	}

	@Test
	public void testUpdateAdminWithNullFields() {
		// Act
		Admin result = adminService.updateAdmin(1L, new Admin());

		// Assert
		assertNull(result);
		verify(adminRepository, never()).save(any(Admin.class));
	}

	@Test
	public void testDeleteAdminWithInvalidId() {
		// Arrange
		Long adminId = -1L;

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertFalse(result);
		verify(adminRepository, never()).deleteById(adminId);
	}

	@Test
	public void testDeleteAdminWithNullId() {
		// Arrange
		Long adminId = null;

		// Act
		boolean result = adminService.deleteAdmin(adminId);

		// Assert
		assertFalse(result);
		verify(adminRepository, never()).deleteById(any());
	}

	@Test
	public void testGetAdminsByEmptyList() {
		// Arrange
		when(adminRepository.findAll()).thenReturn(Collections.emptyList());

		// Act
		List<Admin> result = adminService.getAllAdmins();

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(adminRepository, times(1)).findAll();
	}

}
