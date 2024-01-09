package com.microservice.rover.services;

import com.microservice.rover.entities.Rover;
import com.microservice.rover.exceptions.InvalidCommandException;
import com.microservice.rover.exceptions.RoverNotFoundException;
import com.microservice.rover.repositories.RoverRepository;
import com.microservice.rover.services.client.obstacle.ObstacleClient;
import com.microservice.rover.services.client.obstacle.ObstacleDTO;
import com.microservice.rover.services.client.planet.PlanetClient;
import com.microservice.rover.services.wsdl.GetPlanetResponse;
import com.microservice.rover.services.wsdl.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RemoteControlService {

    public static final char MOVE_FORDWARD = 'f';
    public static final char MOVE_BACKWARD = 'b';
    public static final char TURN_LEFT = 'l';
    public static final char TURN_RIGHT = 'r';

    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';

    private Rover rover;

    private Planet planet;
    private List<ObstacleDTO> obstacles;
    private ObstacleDTO reportedObstacle;

    private final RoverRepository roverRepository;
    private final ObstacleClient obstacleClient;
    private final PlanetClient planetClient;

    @Autowired
    public RemoteControlService(RoverRepository roverRepository, ObstacleClient obstacleClient, PlanetClient planetClient) {
        this.roverRepository = roverRepository;
        this.obstacleClient = obstacleClient;
        this.planetClient = planetClient;
    }

    public ExecuteCommandsResponse executeCommands(List<Character> commands, Long roverId){

        for(Character command : commands){
            if(!isValidCommand(command)){
                throw new InvalidCommandException(command);
            }
        }

        Optional<Rover> optionalRover = roverRepository.findById(roverId);

        if(optionalRover.isEmpty()){
            throw new RoverNotFoundException(roverId);
        }
        rover = optionalRover.get();
        Long planetId = rover.getPlanetId();

        GetPlanetResponse response = planetClient.getPlanet(planetId);
        planet = response.getRetrievedPlanet();

        obstacles = obstacleClient.getAllObstaclesFromPlanet(planetId);

        for(Character command : commands){
            switch (command){
                case MOVE_FORDWARD -> moveForward();
                case MOVE_BACKWARD -> moveBackward();
                case TURN_RIGHT -> turnRight();
                case TURN_LEFT -> turnLeft();
            }
        }

        roverRepository.save(rover);

        return getExecuteCommandsResponse();
    }

    private ExecuteCommandsResponse getExecuteCommandsResponse() {
        String message;

        if(reportedObstacle !=null){
            message = "Mission aborted due to obstacle at coordinates: " +
                    "(" + reportedObstacle.getX() + ", " + reportedObstacle.getY() + ")";
        }else{
            message = "Successful operation";
        }

        ExecuteCommandsResponse commandsResponse = new ExecuteCommandsResponse();
        commandsResponse.setReportedObstacle(reportedObstacle);
        commandsResponse.setUpdatedRover(rover);
        commandsResponse.setMessage(message);
        return commandsResponse;
    }

    private void turnRight() {
        switch (rover.getOrientation()){
            case NORTH -> rover.setOrientation(EAST);
            case SOUTH -> rover.setOrientation(WEST);
            case EAST -> rover.setOrientation(SOUTH);
            case WEST -> rover.setOrientation(NORTH);
        }
    }

    private void turnLeft() {
        switch (rover.getOrientation()){
            case NORTH -> rover.setOrientation(WEST);
            case SOUTH -> rover.setOrientation(EAST);
            case EAST -> rover.setOrientation(NORTH);
            case WEST -> rover.setOrientation(SOUTH);
        }
    }

    private void moveForward() {
        int planetDimensionX = planet.getDimensionX().intValue();
        int x = rover.getX();
        int y = rover.getY();

        switch (rover.getOrientation()) {
            case NORTH -> {
                if (onNorthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(rover.getX() + planetDimensionX/2,rover.getY())){
                            break;
                        }
                        x+=planetDimensionX/2;
                        rover.setOrientation(SOUTH);
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - planetDimensionX/2,y)){
                            break;
                        }
                        x-=planetDimensionX/2;
                        rover.setOrientation(SOUTH);
                    }
                } else {
                    if(reportObstacle(x,y-1)){
                        break;
                    }
                    y--;
                }
            }
            case SOUTH -> {
                if (onSouthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + planetDimensionX/2,y)){
                            break;
                        }
                        x+=planetDimensionX/2;
                        rover.setOrientation(NORTH);

                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - planetDimensionX/2,y)){
                            break;
                        }
                        x-=planetDimensionX/2;
                        rover.setOrientation(NORTH);
                    }
                } else {
                    if(reportObstacle(x,y+1)){
                        break;
                    }
                    y++;
                }
            }
            case EAST -> {
                if (x == planet.getDimensionX().intValue()) {
                    if(reportObstacle(1,y)){
                        break;
                    }
                    x=1;
                } else {
                    if(reportObstacle(x+1,y)){
                        break;
                    }
                    x++;
                }
            }
            case WEST -> {
                if (x == 1) {
                    if(reportObstacle(planetDimensionX,y)){
                        break;
                    }
                    x=planetDimensionX;
                } else {
                    if(reportObstacle(x - 1,y)){
                        break;
                    }
                    x--;
                }
            }
        }
        rover.setX(x);
        rover.setY(y);
    }

    private void moveBackward() {
        int mapDimensionX = planet.getDimensionX().intValue();
        int x = rover.getX();
        int y = rover.getY();

        switch (rover.getOrientation()) {
            case NORTH -> {
                if (onSouthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        rover.setOrientation(SOUTH);
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        rover.setOrientation(SOUTH);
                    }
                } else {
                    if(reportObstacle(x,y+1)){
                        break;
                    }
                    y++;
                }
            }
            case SOUTH -> {
                if (onNorthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        rover.setOrientation(NORTH);
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        rover.setOrientation(NORTH);
                    }
                } else {
                    if(reportObstacle(x,y - 1)){
                        break;
                    }
                    y--;
                }
            }
            case EAST -> {
                if (x == 1) {
                    if(reportObstacle(mapDimensionX,y)){
                        break;
                    }
                    x = mapDimensionX;
                } else {
                    if(reportObstacle(x - 1,y)){
                        break;
                    }
                    x--;
                }
            }
            case WEST -> {
                if (x == mapDimensionX) {
                    if(reportObstacle(1,y)){
                        break;
                    }
                    x = 1;
                } else {
                    if(reportObstacle(x+1,y)){
                        break;
                    }
                    x++;
                }
            }
        }
        rover.setX(x);
        rover.setY(y);
    }

    private boolean onMeridianLeftSide(){
        int x = rover.getX();
        return x >= 1 && x <= planet.getDimensionY().intValue()/2;
    }

    private boolean onMeridianRightSide(){
        int x = rover.getX();
        return x> planet.getDimensionY().intValue()/2 && x<= planet.getDimensionY().intValue();
    }

    private boolean onNorthPole(){
        return rover.getY()==1;
    }

    private boolean onSouthPole(){
        return rover.getY() == planet.getDimensionY().intValue();
    }
    private boolean reportObstacle(int x, int y){

        ObstacleDTO obstacleAtRoverCoordinates = new ObstacleDTO(x,y);

        if(obstacles.contains(obstacleAtRoverCoordinates)){
            reportedObstacle = new ObstacleDTO(x,y);
            return true;
        }
        return false;
    }

    public static boolean isValidCommand(char command) {
        return command == MOVE_FORDWARD || command == MOVE_BACKWARD || command == TURN_RIGHT || command == TURN_LEFT;
    }

}
