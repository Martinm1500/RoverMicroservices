package com.microservice.planet.endpoint;

import com.microservice.planet.faults.InvalidPlanetDimensionsException;
import com.microservice.planet.faults.PlanetNotFoundException;
import com.microservice.planet.services.PlanetServiceImpl;
import com.microservice.planets.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PlanetEndpoint {

    private static final String NAMESPACE_URI = "http://microservice.com/planets";

    private final PlanetServiceImpl planetService;

    @Autowired
    public PlanetEndpoint(PlanetServiceImpl planetService) {
        this.planetService = planetService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPlanetRequest")
    @ResponsePayload
    public CreatePlanetResponse createPlanet(@RequestPayload CreatePlanetRequest request) {
        CreatePlanetResponse response = new CreatePlanetResponse();
        try{
            response = planetService.createPlanet(request);
            response.setStatus(HttpStatus.OK.toString());
            response.setMessage("Planet was successfully created");
        }catch (InvalidPlanetDimensionsException e){
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePlanetRequest")
    @ResponsePayload
    public DeletePlanetResponse deletePlanet(@RequestPayload DeletePlanetRequest request) {
        DeletePlanetResponse response = new DeletePlanetResponse();
        try{
            response = planetService.deletePlanet(request);
            response.setStatus(HttpStatus.OK.toString());
            return response;
        } catch (PlanetNotFoundException e){

            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPlanetRequest")
    @ResponsePayload
    public GetPlanetResponse getPlanet(@RequestPayload GetPlanetRequest request) {
        GetPlanetResponse response = new GetPlanetResponse();
        try{
            response = planetService.getPlanet(request);
            response.setStatus(HttpStatus.OK.toString());
            return response;
        } catch (PlanetNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllPlanets")
    @ResponsePayload
    public GetAllPlanetsResponse getAllPlanets() {
        return planetService.getAllPlanets();
    }

}
