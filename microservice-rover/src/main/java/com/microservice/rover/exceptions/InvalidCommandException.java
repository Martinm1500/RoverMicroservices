package com.microservice.rover.exceptions;

public class InvalidCommandException extends RuntimeException{
    public InvalidCommandException(Character command) {
        super("Invalid command: " + command);
    }
}
