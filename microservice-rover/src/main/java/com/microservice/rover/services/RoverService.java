package com.microservice.rover.services;

import com.microservice.rover.entities.Rover;
import com.microservice.rover.exceptions.InvalidCoordinatesException;
import com.microservice.rover.exceptions.InvalidOrientationException;
import com.microservice.rover.exceptions.RoverNotFoundException;
import com.microservice.rover.exceptions.InvalidOperationException;
import com.microservice.rover.exceptions.PlanetNotFoundException;
public interface RoverService {

    /**
     * Creates a rover on a planet.
     *
     * @param rover  The rover to create.
     * @return The created rover.
     * @throws PlanetNotFoundException         If the planet with the specified ID is not found.
     * @throws InvalidOperationException       If the map already has a rover
     * @throws InvalidCoordinatesException     If the coordinates of the rover do not represent a valid position on the map.
     * @throws InvalidCoordinatesException     If the coordinates coincide with the position of an obstacle.
     * @throws InvalidOrientationException     if the orientation of the rover do not represent a valid orientation
     */
    Rover createRover(Rover rover) throws PlanetNotFoundException, InvalidCoordinatesException, InvalidOrientationException;

    /**
     * Deletes a rover.
     *
     * @param roverId The ID of the rover to delete.
     * @throws RoverNotFoundException  If the rover to be deleted is not found.
     */
    void deleteRover(Long roverId) throws RoverNotFoundException;

    /**
     * Updates the position and orientation of a rover.
     * @return The updated rover.
     * @param rover The rover with the updated position and orientation.
     * @throws RoverNotFoundException     If the rover is not found.
     * @throws InvalidCoordinatesException  If the coordinates of the rover do not represent a valid position on the map.
     * @throws InvalidCoordinatesException  If the coordinates coincide with the position of an obstacle.
     * @throws InvalidOrientationException if the orientation of rover do not represent a valid orientation
     */
    Rover updateRover(Rover rover) throws RoverNotFoundException, InvalidCoordinatesException, InvalidOrientationException;

    /**
     * Gets a rover with the specified ID.
     *
     * @param roverId The ID of the rover to get.
     * @return The found rover.
     * @throws RoverNotFoundException  If the rover does not exist.
     */
    Rover getRover(Long roverId) throws RoverNotFoundException;

    /**
     * Gets a rover associated with a specific planet.
     *
     * @param planetId The ID of the planet
     * @return The rover associated with the planet.
     * @throws PlanetNotFoundException If the planet with the provided ID does not exist or if there is no rover associated with the planet.
     */
    Rover getRoverByPlanetId(Long planetId) throws PlanetNotFoundException;
}
