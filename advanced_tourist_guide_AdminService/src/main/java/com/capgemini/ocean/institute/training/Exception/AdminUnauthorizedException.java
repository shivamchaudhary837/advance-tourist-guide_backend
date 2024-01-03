package com.capgemini.ocean.institute.training.Exception;

@SuppressWarnings("serial")
public class AdminUnauthorizedException extends RuntimeException {

    public AdminUnauthorizedException(String message) {
        super(message);
    }
}