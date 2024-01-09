package com.microservice.rover.services;

import com.microservice.rover.entities.Rover;
import com.microservice.rover.exceptions.*;
import com.microservice.rover.repositories.RoverRepository;
import com.microservice.rover.services.client.obstacle.ObstacleClient;
import com.microservice.rover.services.client.obstacle.ObstacleDTO;
import com.microservice.rover.services.client.planet.PlanetClient;
import com.microservice.rover.services.wsdl.GetPlanetResponse;
import com.microservice.rover.services.wsdl.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class RoverServiceImpl implements RoverService{

    private final RoverRepository roverRepository;

    private final ObstacleClient obstacleClient;

    private final PlanetClient planetClient;

    @Autowired
    public RoverServiceImpl(RoverRepository roverRepository, PlanetClient planetClient, ObstacleClient obstacleClient) {
        this.planetClient = planetClient;
        this.roverRepository = roverRepository;
        this.obstacleClient = obstacleClient;
    }

    @Override
    public Rover createRover(Rover rover){
        Long planetId = rover.getPlanetId();

        GetPlanetResponse response = planetClient.getPlanet(planetId);
        Planet planet = response.getRetrievedPlanet();

        if(planet == null){
            throw new PlanetNotFoundException(planetId);
        }

        Optional<Rover> optionalRoverFound = roverRepository.findByPlanetId(planetId);

        if(optionalRoverFound.isPresent()){
            throw new InvalidOperationException("Planet with id:" +planetId + ", already has a rover");
        }

        BigInteger x = planet.getDimensionX();
        BigInteger y = planet.getDimensionY();

        int coordinateX = rover.getX();
        int coordinateY = rover.getY();

        if(coordinateX <= 0 || coordinateX > x.intValue() || coordinateY <= 0 || coordinateY > y.intValue()){
            throw new InvalidCoordinatesException(coordinateX,coordinateY);
        }

        ObstacleDTO obstacleAtRoverCoordinates = new ObstacleDTO(coordinateX, coordinateY);
        List<ObstacleDTO> obstacleDTOList = obstacleClient.getAllObstaclesFromPlanet(planetId);

        if(obstacleDTOList.contains(obstacleAtRoverCoordinates)){
            throw new InvalidCoordinatesException("An obstacle already exists at coordinates: " + coordinateX + ", " + coordinateY);
        }

        if(!Rover.isValidOrientation(rover.getOrientation())){
            throw new InvalidOrientationException(rover.getOrientation());
        }

        return roverRepository.save(rover);

    }

    @Override
    public void deleteRover(Long roverId){
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if(optionalRover.isPresent()){
            roverRepository.deleteById(roverId);
        }else{
            throw new RoverNotFoundException(roverId);
        }
    }

    @Override
    public Rover updateRover(Rover rover){

        Long roverId = rover.getId();

        if(!roverRepository.existsById(roverId)){
            throw new RoverNotFoundException(roverId);
        }

        if(Rover.isValidOrientation(rover.getOrientation())){
            throw new InvalidOrientationException(rover.getOrientation());
        }

        Long planetId = rover.getPlanetId();

        GetPlanetResponse response = planetClient.getPlanet(planetId);
        Planet planet = response.getRetrievedPlanet();


        int coordinateX = rover.getX();
        int coordinateY = rover.getY();

        BigInteger x = planet.getDimensionX();
        BigInteger y = planet.getDimensionY();

        if(coordinateX <= 0 || coordinateX > x.intValue() || coordinateY <= 0 || coordinateY > y.intValue()){
            throw new InvalidCoordinatesException(coordinateX,coordinateY);
        }

        ObstacleDTO obstacleInRoverCoordinates = new ObstacleDTO(coordinateX, coordinateY);
        List<ObstacleDTO> obstacleDTOList = obstacleClient.getAllObstaclesFromPlanet(planetId);

        if(obstacleDTOList.contains(obstacleInRoverCoordinates)){
            throw new InvalidCoordinatesException("An obstacle already exists at coordinates: " + coordinateX + ", " + coordinateY);
        }

        return roverRepository.save(rover);

    }

    @Override
    public Rover getRover(Long roverId){
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if(optionalRover.isPresent()){
            return optionalRover.get();
        }else {
            throw new RoverNotFoundException(roverId);
        }
    }

    @Override
    public Rover getRoverByPlanetId(Long planetId){
        Optional<Rover> optionalRover = roverRepository.findByPlanetId(planetId);
        if(optionalRover.isEmpty()){
            throw new RoverNotFoundException(planetId);
        }
        return optionalRover.get();
    }
}
