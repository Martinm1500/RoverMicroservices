package com.microservice.rover.controllers;

import com.microservice.rover.dtos.RoverDTO;
import com.microservice.rover.entities.Rover;
import com.microservice.rover.exceptions.*;
import com.microservice.rover.services.RoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rovers")
public class RoverController {

    private final RoverService roverService;

    @Autowired
    public RoverController(RoverService roverService) {
        this.roverService = roverService;
    }

    @PostMapping("/create-on-planet")
    public ResponseEntity<?> createRover(@Valid @RequestBody Rover rover){
        try{
            Rover createdRover = roverService.createRover(rover);
            RoverDTO roverDTO = RoverDTO.convertToDTO(createdRover);

            return ResponseEntity.status(HttpStatus.CREATED).body(roverDTO);
        }catch (PlanetNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidCoordinatesException | InvalidOrientationException | InvalidOperationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roverId}")
    public ResponseEntity<?> deleteRover(@PathVariable Long roverId) {
        try {
            roverService.deleteRover(roverId);
            return ResponseEntity.ok("Rover with ID: " + roverId + " was deleted successfully");
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRover(@Valid @RequestBody Rover rover) {
        try {
            Rover updatedRover = roverService.updateRover(rover);
            RoverDTO roverDTO = RoverDTO.convertToDTO(updatedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidCoordinatesException | InvalidOrientationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<?> getRover(@PathVariable Long roverId) {
        try {
            Rover obtainedRover = roverService.getRover(roverId);
            RoverDTO roverDTO = RoverDTO.convertToDTO(obtainedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/by-planet/{planetId}")
    public ResponseEntity<?> getRoverByPlanetId(@PathVariable Long planetId) {
        try {
            Rover obtainedRover = roverService.getRoverByPlanetId(planetId);
            RoverDTO roverDTO = RoverDTO.convertToDTO(obtainedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
