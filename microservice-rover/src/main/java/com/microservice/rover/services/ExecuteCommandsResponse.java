package com.microservice.rover.services;

import com.microservice.rover.services.client.obstacle.ObstacleDTO;
import com.microservice.rover.entities.Rover;
import lombok.Data;

@Data
public class ExecuteCommandsResponse {

    private ObstacleDTO reportedObstacle;
    private Rover updatedRover;
    private String message;
}
