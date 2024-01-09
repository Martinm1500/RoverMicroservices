package com.microservice.obstacle.services;
import com.microservice.obstacle.exceptions.ObstacleNotFoundException;
import com.microservice.obstacle.exceptions.InvalidCoordinatesException;
import com.microservice.obstacle.exceptions.PlanetNotFoundException;
import com.microservice.obstacle.entities.Obstacle;

import java.util.List;

public interface ObstacleService {

    /**
     * Create an obstacle to a planet.
     *
     * @param obstacle The obstacle to create.
     * @return The added obstacle.
     * @throws PlanetNotFoundException         If the planet is not found.
     * @throws InvalidCoordinatesException      If the coordinates of the obstacle do not represent a valid position on the planet.
     */
    Obstacle createObstacle(Obstacle obstacle) throws Exception;

    /**
     * Deletes an obstacle with the specified ID.
     *
     * @param obstacleId The ID of the obstacle to delete.
     * @throws ObstacleNotFoundException     If the obstacle is not found.
     */
    void deleteObstacle(Long obstacleId) throws ObstacleNotFoundException;

    /**
     * Gets an obstacle with the specified ID.
     *
     * @param obstacleId The ID of the obstacle to get.
     * @return The found obstacle.
     * @throws ObstacleNotFoundException If the obstacle is not found.
     */
    Obstacle getObstacle(Long obstacleId) throws ObstacleNotFoundException;

    /**
     * Returns all obstacles that are on a map.
     *
     * @param mapId The ID of the map.
     * @return List of obstacles on the map.
     * @throws PlanetNotFoundException If the map does not exist.
     */
    List<Obstacle> getAllByPlanetId(Long mapId);
}