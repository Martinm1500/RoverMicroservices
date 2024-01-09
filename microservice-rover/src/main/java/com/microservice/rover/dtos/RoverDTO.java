package com.microservice.rover.dtos;

import com.microservice.rover.entities.Rover;
import lombok.Data;

@Data
public class RoverDTO {
    private Long id;
    private int x;
    private int y;
    private char orientation;
    private Long planetId;
    public static RoverDTO convertToDTO(Rover rover) {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setId(rover.getId());
        roverDTO.setX(rover.getX());
        roverDTO.setY(rover.getY());
        roverDTO.setPlanetId(rover.getPlanetId());
        roverDTO.setOrientation(rover.getOrientation());
        return roverDTO;
    }
}
