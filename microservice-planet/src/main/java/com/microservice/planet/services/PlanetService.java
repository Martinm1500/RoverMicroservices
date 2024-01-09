package com.microservice.planet.services;


import com.microservice.planets.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public interface PlanetService {

    @WebMethod
    CreatePlanetResponse createPlanet(
            @WebParam(name = "createPlanetRequest") CreatePlanetRequest createPlanetRequest
    );

    @WebMethod
    DeletePlanetResponse deletePlanet(
            @WebParam(name = "deletePlanetRequest") DeletePlanetRequest deletePlanetRequest
    );

    @WebMethod
    GetPlanetResponse getPlanet(
            @WebParam(name = "getPlanetRequest") GetPlanetRequest getPlanetRequest
    );

    @WebMethod
    GetAllPlanetsResponse getAllPlanets();

}



