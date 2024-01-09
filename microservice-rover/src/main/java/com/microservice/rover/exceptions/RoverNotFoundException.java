package com.microservice.rover.exceptions;

public class RoverNotFoundException extends RuntimeException{

    public RoverNotFoundException(Long roverId) {
        super("Could not find rover with ID: " + roverId);
    }

}
