package com.capgemini.ocean.institute.training.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.ocean.institute.training.dto.UserDto;
import com.capgemini.ocean.institute.training.exception.EmailIdAlreadyExistsException;
import com.capgemini.ocean.institute.training.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = this.userService.createUser(userDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (EmailIdAlreadyExistsException e) {
            return new ResponseEntity<>("Email already exists!", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usersByRole/{role}")
    public ResponseEntity<?> getAllUsersByRole(@PathVariable String role) {
        try {
            List<UserDto> userDtos = this.userService.getAllUsersByRole(role);
            return new ResponseEntity<>(userDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        try {
            return this.userService.loginUser(userDto);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        try {
            UserDto userDto = this.userService.getUserById(userId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> viewAllUsers() {
        try {
            List<UserDto> userDtos = this.userService.getAllUsers();
            return new ResponseEntity<>(userDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
