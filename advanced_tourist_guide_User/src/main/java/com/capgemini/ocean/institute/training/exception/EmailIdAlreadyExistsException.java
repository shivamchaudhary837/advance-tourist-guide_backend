package com.capgemini.ocean.institute.training.exception;



@SuppressWarnings("serial")
public class EmailIdAlreadyExistsException extends RuntimeException {

    public EmailIdAlreadyExistsException() {
        super(String.format("Email already exists!"));
    }
}




