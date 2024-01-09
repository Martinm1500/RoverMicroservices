package com.microservice.rover.repositories;


import com.microservice.rover.entities.Rover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoverRepository extends JpaRepository<Rover,Long> {
    Optional<Rover> findByPlanetId(Long planetId);
}
