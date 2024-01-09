package com.microservice.rover.exceptions;

public class InvalidOrientationException extends RuntimeException{
    public InvalidOrientationException(char orientation) {
        super("The character '" + orientation + "' does not represent a valid orientation.");
    }
}
