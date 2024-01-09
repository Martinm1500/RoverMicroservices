package com.microservice.planet.faults;

public class PlanetNotFoundException extends RuntimeException {

    public PlanetNotFoundException(Long id) {
        super("Planet with ID " + id + " not found.");
    }

}
