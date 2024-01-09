package com.microservice.rover.controllers;


import com.microservice.rover.exceptions.InvalidCommandException;
import com.microservice.rover.exceptions.RoverNotFoundException;
import com.microservice.rover.services.ExecuteCommandsResponse;
import com.microservice.rover.services.RemoteControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rover-control")
public class RemoteControlController {

    private final RemoteControlService remoteControlService;

    @Autowired
    public RemoteControlController(RemoteControlService remoteControlService) {
        this.remoteControlService = remoteControlService;
    }

    @PostMapping("/execute-commands/{roverId}")
    public ResponseEntity<?> executeCommands(@PathVariable Long roverId, @RequestBody List<Character> commands) {
        try {
            ExecuteCommandsResponse response = remoteControlService.executeCommands(commands,roverId);
            if(response.getReportedObstacle()!= null){
                // Mission aborted due to an obstacle. Reporting and returning the obstacle.
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response.getMessage());
            }
            return ResponseEntity.ok().body(response.getMessage());
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidCommandException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
