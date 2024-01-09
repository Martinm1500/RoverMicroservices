package com.microservice.obstacle.exceptions;

public class ObstacleNotFoundException extends RuntimeException{
    public ObstacleNotFoundException(Long obstacleID) {

        super("Obstacle with ID: " + obstacleID + ". No found");
    }
}
