package com.microservice.obstacle.dtos;

import com.microservice.obstacle.entities.Obstacle;
import lombok.Data;

@Data
public class ObstacleDTO {

    private int x;
    private int y;
    private Long id;

    public static ObstacleDTO convertToDTO(Obstacle obstacle) {
        ObstacleDTO obstacleDTO = new ObstacleDTO();
        obstacleDTO.setX(obstacle.getX());
        obstacleDTO.setY(obstacle.getY());
        obstacleDTO.setId(obstacle.getId());
        return obstacleDTO;
    }
}
