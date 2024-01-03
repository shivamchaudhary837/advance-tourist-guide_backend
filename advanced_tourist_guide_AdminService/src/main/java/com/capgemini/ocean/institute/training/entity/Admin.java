package com.capgemini.ocean.institute.training.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Admin  {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;
 
 private String email;
 
 private String password;
 

 // Constructors, getters, and setters

 public Admin() {
 }

 public Admin(Long id,String name, String password,String email) {
	 this.id=id;
     this.name = name;
     this.password = password;
     this.email=email;
 }

 // Getters and setters

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

 public String getPassword() {
     return password;
 }
 public void setEmail(String email) {
	 this.email=email;
 }
 public String getEamil() {
	 return email;
 }

 public void setPassword(String password) {
     this.password = password;
 }

@Override
public String toString() {
	return "Admin [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
}
 
}
