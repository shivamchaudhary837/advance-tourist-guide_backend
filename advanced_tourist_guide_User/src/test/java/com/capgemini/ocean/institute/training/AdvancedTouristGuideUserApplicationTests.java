package com.capgemini.ocean.institute.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.ocean.institute.training.config.JwtAuthEntrypoint;
import com.capgemini.ocean.institute.training.dto.UserDto;
import com.capgemini.ocean.institute.training.entites.User;
import com.capgemini.ocean.institute.training.exception.EmailIdAlreadyExistsException;
import com.capgemini.ocean.institute.training.jwt.JwtTokenProvider;
import com.capgemini.ocean.institute.training.repository.UserRepository;
import com.capgemini.ocean.institute.training.service.implementation.UserServiceImpl;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

public class AdvancedTouristGuideUserApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;
    
    @InjectMocks
    private JwtAuthEntrypoint jwtAuthEntrypoint;
    
    @SuppressWarnings("unused")
	@Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    

    @Test
    public void testCreateUserWithExistingEmail() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("test@example.com");
        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(EmailIdAlreadyExistsException.class, () -> userService.createUser(userDto));
        verify(userRepository, times(1)).findByEmailId(userDto.getEmailId());
        verify(modelMapper, never()).map(any(), any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    

    

    @Test
    public void testGetUserById() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

   
    @Test
    public void testGetUserByIdNotFound() {
        // Arrange
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, never()).map(any(), eq(UserDto.class));
    }

   
   
   
    @Test
    public void testCreateTokenForSingleSignOn() {
        // Arrange
        String username = "testuser";
        when(jwtTokenProvider.createTokenForSingleSignOn(username)).thenReturn("generatedToken");

        // Act
        String result = jwtTokenProvider.createTokenForSingleSignOn(username);

        // Assert
        assertNotNull(result);
        assertEquals("generatedToken", result);
        verify(jwtTokenProvider, times(1)).createTokenForSingleSignOn(username);
    }

    @Test
    public void testCreateUserWithValidData() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("test@example.com");
        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(new User());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act
        userService.createUser(userDto);

        // Assert
        verify(userRepository, times(1)).findByEmailId(userDto.getEmailId());
        verify(modelMapper, times(1)).map(userDto, User.class);
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any(User.class));
    }

    
    

    @Test
    public void testDeleteUser() {
        // Arrange
        int userId = 1;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

   

   
    @Test
    public void testUpdateUserNotFound() {
        // Arrange
        UserDto userDto = new UserDto();
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(userDto, userId));
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, never()).map(any(), eq(User.class));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> userList = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(userList, List.class)).thenReturn(Arrays.asList(new UserDto(), new UserDto()));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(userList, List.class);
    }

    @Test
    public void testGetAllUsersByRole() {
        // Arrange
        String role = "ROLE_USER";
        List<User> userList = Arrays.asList(new User(), new User());
        when(userRepository.findAllByRole(role)).thenReturn(userList);
        when(modelMapper.map(userList, List.class)).thenReturn(Arrays.asList(new UserDto(), new UserDto()));

        // Act
        List<UserDto> result = userService.getAllUsersByRole(role);

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAllByRole(role);
        verify(modelMapper, times(1)).map(userList, List.class);
    }

    
    @SuppressWarnings({ "serial", "unlikely-arg-type" })
	@Test
    public void testLoginUserWithInvalidCredentials() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("kushwahamanish311@gmail.com");
        userDto.setPassword("invalidPassword");

        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.of(new User()));
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        equals("User not found with email id:");
    }

    @Test
    public void testGetUserByEmail() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmailId(email);
        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmailId(email);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

    @Test
    public void testGetUserByEmailNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmailId(email)).thenReturn(Optional.empty());

        // Act and Assert
        UserDto userDto = userService.getUserByEmail(email);
        assertNull(userDto);

        verify(userRepository, times(1)).findByEmailId(email);
        verify(modelMapper, never()).map(any(), eq(UserDto.class));
    }



    

    @Test
    public void testUpdateUserWithNonexistentUserId() {
        // Arrange
        int userId = 999;
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Updated Name");
        updatedUserDto.setEmailId("updated@example.com");
        updatedUserDto.setPassword("updatedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(updatedUserDto, userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
        verify(modelMapper, never()).map(any(), eq(UserDto.class));
    }

   
    

    @Test
    public void testCreateUserWithValidDataAndRole() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("newuser@example.com");
        userDto.setPassword("strongPassword");
        userDto.setRole("ROLE_USER");

        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(new User());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act
        userService.createUser(userDto);

        // Assert
        verify(userRepository, times(1)).findByEmailId(userDto.getEmailId());
        verify(modelMapper, times(1)).map(userDto, User.class);
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUsersWithValidData() {
        // Arrange
        List<User> userList = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(userList, List.class)).thenReturn(Arrays.asList(new UserDto(), new UserDto()));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(userList, List.class);
    }

    @Test
    public void testGetAllUsersByRoleWithValidData() {
        // Arrange
        String role = "ROLE_ADMIN";
        List<User> userList = Arrays.asList(new User(), new User());
        when(userRepository.findAllByRole(role)).thenReturn(userList);
        when(modelMapper.map(userList, List.class)).thenReturn(Arrays.asList(new UserDto(), new UserDto()));

        // Act
        List<UserDto> result = userService.getAllUsersByRole(role);

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAllByRole(role);
        verify(modelMapper, times(1)).map(userList, List.class);
    }

    @Test
    public void testGetUserByIdWithValidData() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }


    

   
    @Test
    public void testGetUserByEmailWithValidData() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmailId(email);
        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmailId(email);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

    
    

   
    @Test
    public void testGetUserByEmailWithValidDataAndRole() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmailId(email);
        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmailId(email);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

   
   
    @Test
    public void testGetAllUsersWithEmptyUserList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(modelMapper.map(Collections.emptyList(), List.class)).thenReturn(Collections.emptyList());

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(Collections.emptyList(), List.class);
    }

    @Test
    public void testGetAllUsersByRoleWithEmptyUserList() {
        // Arrange
        String role = "ROLE_USER";
        when(userRepository.findAllByRole(role)).thenReturn(Collections.emptyList());
        when(modelMapper.map(Collections.emptyList(), List.class)).thenReturn(Collections.emptyList());

        // Act
        List<UserDto> result = userService.getAllUsersByRole(role);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAllByRole(role);
        verify(modelMapper, times(1)).map(Collections.emptyList(), List.class);
    }

   
   

   

    @Test
    public void testDeleteUserWithValidUserId() {
        // Arrange
        int userId = 1;

        // Act
        userService.deleteUser(userId);

        // Verify interactions
        verify(userRepository, times(1)).deleteById(userId);
    }

    
    @Test
    void commenceShouldSetUnauthorizedResponse() throws IOException, java.io.IOException, ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Authentication Failed") {};

        // Act
        jwtAuthEntrypoint.commence(request, response, authException);

        // Assert
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Authentication Failed", response.getContentAsString());
    }

    @Test
    void handleAccessDeniedShouldSetForbiddenResponse() throws IOException, java.io.IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

        // Act
        jwtAuthEntrypoint.commence(request, response, accessDeniedException);

        // Assert
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals("Access Denies: Access Denied", response.getContentAsString());
    }

    @Test
    void handleNotFoundExceptionShouldSetNotFoundResponse() throws IOException, java.io.IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChangeSetPersister.NotFoundException notFoundException = new ChangeSetPersister.NotFoundException();

        // Act
        jwtAuthEntrypoint.commence(request, response, notFoundException);

        // Assert
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals("Not found: null", response.getContentAsString()); // Adjust the expected message based on your implementation
    }

   

   

   



    @Test
    void validateExpiredTokenShouldReturnFalse() {
        // Arrange
        String username = "testUser";
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "", authorities);

        // Create a token with a very short validity period
        String token = jwtTokenProvider.createToken(username, authorities); // Fix the argument

        // Act (wait for the token to expire)
        try {
            Thread.sleep(2); // Sleep for 2 milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

   
    @Test
    void validateTokenWithInvalidTokenShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalidToken";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

   

    @Test
    void validateTokenWithMissingSubjectShouldReturnFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJyb2xlcyIsImV4cCI6MTY0NDk0NjI4OCwiaXNzIjoibG9naW4iLCJpYXQiOjE2NDQ5NDYyNTksInN1YiI6IldobyIsInJvbGVzIjoidXNlciJ9.2T3FJf56ac4EVF4lA1vZlyjtH5pRSDcGi__FgjeYcq8";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

   
   

    

    
    

   

    @Test
    public void testLoginUserWithInvalidEmail() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("kushwahamanish311@gmail.com");
        userDto.setPassword("validPassword");

        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        equals("User not found with email id:");
    }

	@Test
	public void testLoginUserWithInvalidPassword() {
		// Arrange
		UserDto userDto = new UserDto();
		userDto.setEmailId("kushwahamanish311@gmail.com");
		userDto.setPassword("invalidPassword");
		User user = new User();
		user.setPassword("encodedPassword");

		when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(false);

		// Act
		ResponseEntity<?> responseEntity = userService.loginUser(userDto);

		// Assert
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(responseEntity.getBody() instanceof String);
		equals("User not found with email id:");
	}


    @Test
    public void testLoginUserWithNonexistentEmail() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("nonexistent@gmail.com");

        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Password cannot be empty", responseEntity.getBody());
    }


    @Test
    public void testLoginUserWithNullEmail() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPassword("validPassword");

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Email cannot be empty", responseEntity.getBody());
    }

   
    @Test
    public void testLoginUserWithNullPassword() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("kushwahamanish311@gmail.com");

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Password cannot be empty", responseEntity.getBody());
    }

    @Test
    public void testLoginUserWithEmptyEmailAndPassword() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Password cannot be empty", responseEntity.getBody());
    }

    @Test
    public void testLoginUserWithEmptyEmailAndValidPassword() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPassword("validPassword");

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Email cannot be empty", responseEntity.getBody());
    }

    @Test
    public void testLoginUserWithValidEmailAndEmptyPassword() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmailId("kushwahamanish311@gmail.com");

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Password cannot be empty", responseEntity.getBody());
    }

    @Test
    public void testLoginUserWithEmptyEmailAndInvalidPassword() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPassword("invalidPassword");

        // Act
        ResponseEntity<?> responseEntity = userService.loginUser(userDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("Email cannot be empty", responseEntity.getBody());
    }


}
    
   

   

   

    

   

   
  

    


   

   
   
   

    
    

    

   

   

   
   

    

    

   

   


  

    
   


    

    

    
    
   

    



