package com.microservice.planet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroservicePlanetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicePlanetApplication.class, args);
	}
}