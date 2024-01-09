package com.microservice.rover.services.client.obstacle;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="msvc-obstacle", url= "localhost:9090/api/obstacles")
public interface ObstacleClient {

    @GetMapping("/get/all-from-planet/{planetId}")
    List<ObstacleDTO> getAllObstaclesFromPlanet(@PathVariable Long planetId);
}
