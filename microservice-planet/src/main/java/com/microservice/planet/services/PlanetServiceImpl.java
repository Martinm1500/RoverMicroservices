package com.microservice.planet.services;

import com.microservice.planet.faults.InvalidPlanetDimensionsException;
import com.microservice.planet.faults.PlanetNotFoundException;
import com.microservice.planet.repository.PlanetRepository;
import com.microservice.planets.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetServiceImpl(PlanetRepository mapRepository) {
        this.planetRepository = mapRepository;
    }

    @Override
    public CreatePlanetResponse createPlanet(CreatePlanetRequest createPlanetRequest) {
        Planet planet = createPlanetRequest.getPlanet();
        BigInteger x = planet.getDimensionX();
        BigInteger y = planet.getDimensionY();

        if (!isValidDimensions(x, y)) {
            throw new InvalidPlanetDimensionsException(x, y);
        }

        Planet createdPlanet = planetRepository.createPlanet(planet);

        CreatePlanetResponse response = new CreatePlanetResponse();
        response.setCreatedPlanet(createdPlanet);
        return response;
    }

    @Override
    public DeletePlanetResponse deletePlanet(DeletePlanetRequest deletePlanetRequest) {
        long planetId = deletePlanetRequest.getPlanetId();
        long deletedPlanetId = planetRepository.deletePlanet(planetId);

        if(deletedPlanetId != planetId){
            throw new PlanetNotFoundException(planetId);
        }

        DeletePlanetResponse response = new DeletePlanetResponse();
        response.setMessage("Planet with id:"+planetId+" was successfully deleted");

        return response;
    }

    @Override
    public GetPlanetResponse getPlanet(GetPlanetRequest getPlanetRequest) {
        long planetId = getPlanetRequest.getPlanetId();
        Optional<Planet> optionalPlanet = planetRepository.getPlanet(planetId);
        if(optionalPlanet.isEmpty()){
            throw new PlanetNotFoundException(planetId);
        }
        GetPlanetResponse response = new GetPlanetResponse();
        response.setRetrievedPlanet(optionalPlanet.get());
        return response;
    }

    @Override
    public GetAllPlanetsResponse getAllPlanets() {
        List<Planet> allPlanets = planetRepository.getAllPlanets();
        GetAllPlanetsResponse response = new GetAllPlanetsResponse();
        response.getPlanets().addAll(allPlanets);
        return response;
    }

    private boolean isValidDimensions(BigInteger dimensionX, BigInteger dimensionY) {

        return dimensionX.compareTo(BigInteger.ZERO) > 0 && dimensionY.compareTo(BigInteger.ZERO) > 0 && isEven(dimensionX) && isEven(dimensionY);
    }

    private boolean isEven(BigInteger number) {

        return number.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO);
    }

}
