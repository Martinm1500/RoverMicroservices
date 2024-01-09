package com.microservice.planet.faults;

import javax.xml.ws.WebFault;
import java.math.BigInteger;

@WebFault(name = "InvalidPlanetDimensionsException")
public class InvalidPlanetDimensionsException extends RuntimeException {

    public InvalidPlanetDimensionsException(BigInteger x , BigInteger y){
        super("Invalid planet dimensions: ("+x+","+y+"). Please provide valid dimensions");
    }
}