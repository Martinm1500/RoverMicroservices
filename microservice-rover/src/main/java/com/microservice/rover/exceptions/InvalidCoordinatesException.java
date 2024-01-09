package com.microservice.rover.exceptions;

public class InvalidCoordinatesException extends RuntimeException {

    public InvalidCoordinatesException(int x, int y) {
        super("Coordinates:("+x+","+y+") do not represent a valid position on the planet");

    }

    public InvalidCoordinatesException(String s) {
    }
}
