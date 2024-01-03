package com.capgemini.ocean.institute.training.Exception;

@SuppressWarnings("serial")
public class AdminBadRequestException extends RuntimeException {

    public AdminBadRequestException(String message) {
        super(message);
    }
}
