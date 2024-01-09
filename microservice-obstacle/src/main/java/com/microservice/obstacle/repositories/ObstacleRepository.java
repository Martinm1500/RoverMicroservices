package com.microservice.obstacle.repositories;

import com.microservice.obstacle.entities.Obstacle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObstacleRepository extends JpaRepository<Obstacle,Long> {
    List<Obstacle> findAllByPlanetId(Long planetId);
}
