package com.microservice.obstacle.exceptions;

public class PlanetNotFoundException extends RuntimeException{
    public PlanetNotFoundException(Long planetId) {

        super("The planet with id: " + planetId + " does not exist");
    }
}
