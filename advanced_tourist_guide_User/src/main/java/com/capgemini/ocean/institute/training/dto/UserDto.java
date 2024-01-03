package com.capgemini.ocean.institute.training.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int userId;

	@NotEmpty(message = "Name cannot be empty!")
	@Pattern(regexp = "^[a-zA-Z ]*", message = "Incorrect format for Name!")
	private String name;

	@NotEmpty(message = "Email ID cannot be empty!")
	@Email(message = "Kindly enter correct email id!")
	private String emailId;

	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}|:\"<>?/.,';\\[\\]])[A-Za-z\\d!@#$%^&*()_+=\\-{}|:\"<>?/.,';\\[\\]]+$", message = "Password should contain at least one upper-case letter, lower-case letter, a digit and a special character")
	private String password;
	private String role;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
