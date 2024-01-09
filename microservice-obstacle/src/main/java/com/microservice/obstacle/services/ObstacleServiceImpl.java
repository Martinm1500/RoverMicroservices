package com.microservice.obstacle.services;

import com.microservice.obstacle.entities.Obstacle;
import com.microservice.obstacle.exceptions.InvalidCoordinatesException;
import com.microservice.obstacle.exceptions.ObstacleNotFoundException;
import com.microservice.obstacle.exceptions.PlanetNotFoundException;
import com.microservice.obstacle.repositories.ObstacleRepository;


import com.microservice.obstacle.services.client.PlanetClient;
import com.microservice.obstacle.services.wsdl.GetPlanetResponse;
import com.microservice.obstacle.services.wsdl.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ObstacleServiceImpl implements ObstacleService{

    private final ObstacleRepository obstacleRepository;

    private final PlanetClient planetClient;

    @Autowired
    public ObstacleServiceImpl(ObstacleRepository obstacleRepository, PlanetClient planetClient) {
        this.obstacleRepository = obstacleRepository;
        this.planetClient = planetClient;
    }

    @Override
    public Obstacle createObstacle(Obstacle obstacle) {
        Long planetId = obstacle.getPlanetId();

        GetPlanetResponse response = planetClient.getPlanet(planetId);
        Planet planet = response.getRetrievedPlanet();

        if(planet == null){
            throw new PlanetNotFoundException(planetId);
        }
        BigInteger x = planet.getDimensionX();
        BigInteger y = planet.getDimensionY();

        int coordinateX = obstacle.getX();
        int coordinateY = obstacle.getY();

        if(coordinateX <= 0 || coordinateX > x.intValue() || coordinateY <= 0 || coordinateY > y.intValue()){
            throw new InvalidCoordinatesException(coordinateX,coordinateY);
        }

        obstacle.setPlanetId(planetId);
        return  obstacleRepository.save(obstacle);
    }

    @Override
    public void deleteObstacle(Long obstacleId){
        Optional<Obstacle> obstacleOptional = obstacleRepository.findById(obstacleId);
        if (obstacleOptional.isPresent()) {
            Obstacle obstacle = obstacleOptional.get();
            obstacleRepository.delete(obstacle);
        } else {
            throw new ObstacleNotFoundException(obstacleId);
        }
    }

    @Override
    public Obstacle getObstacle(Long obstacleId){
        Optional<Obstacle> optionalObstacle = obstacleRepository.findById(obstacleId);
        if(optionalObstacle.isPresent()){
            return optionalObstacle.get();
        }else{
            throw new ObstacleNotFoundException(obstacleId);
        }
    }

    @Override
    public List<Obstacle> getAllByPlanetId(Long planetId){
        return obstacleRepository.findAllByPlanetId(planetId);
    }
}
