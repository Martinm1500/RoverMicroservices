package com.microservice.obstacle.controllers;

import com.microservice.obstacle.dtos.ObstacleDTO;
import com.microservice.obstacle.entities.Obstacle;
import com.microservice.obstacle.exceptions.InvalidCoordinatesException;
import com.microservice.obstacle.exceptions.ObstacleNotFoundException;
import com.microservice.obstacle.exceptions.PlanetNotFoundException;
import com.microservice.obstacle.services.ObstacleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/obstacles")
public class ObstacleController {

    private final ObstacleService obstacleService;

    @Autowired
    public ObstacleController(ObstacleService obstacleService) {
        this.obstacleService = obstacleService;
    }

    @PostMapping("/create-on-planet")
    public ResponseEntity<?> createObstacle(@Valid @RequestBody Obstacle obstacle) {
        try {
            Obstacle createdObstacle = obstacleService.createObstacle(obstacle);
            ObstacleDTO obstacleDTO = ObstacleDTO.convertToDTO(createdObstacle);
            return ResponseEntity.status(HttpStatus.CREATED).body(obstacleDTO);
        } catch (PlanetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidCoordinatesException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{obstacleId}")
    public ResponseEntity<?> deleteObstacle(@PathVariable Long obstacleId) {
        try {
            obstacleService.deleteObstacle(obstacleId);
            return ResponseEntity.ok("Obstacle with ID: " + obstacleId + " was deleted successfully");
        } catch (ObstacleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get/all-from-planet/{planetId}")
    public ResponseEntity<?> getAllObstaclesFromPlanet(@PathVariable Long planetId) {
        try {
            List<Obstacle> obstacles = obstacleService.getAllByPlanetId(planetId);
            List<ObstacleDTO> obstacleDTOs = obstacles.stream()
                    .map(ObstacleDTO::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(obstacleDTOs);
        } catch (PlanetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}