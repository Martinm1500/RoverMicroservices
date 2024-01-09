package com.microservice.planet.repository;

import com.microservice.planets.Planet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PlanetRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String table = "planet";

    public PlanetRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Planet createPlanet(Planet planet) {
        String sql = "INSERT INTO " + table + " (name, dimensionX, dimensionY) VALUES (:name, :dimensionX, :dimensionY)";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", planet.getName())
                .addValue("dimensionX", planet.getDimensionX())
                .addValue("dimensionY", planet.getDimensionY());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, parameters, keyHolder, new String[]{"id"});

        if (keyHolder.getKey() != null) {
            planet.setId(keyHolder.getKey().longValue());
        }
        return planet;
    }

    public long deletePlanet(long id) {
        String sql = "DELETE FROM " + table + " WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        int rowsAffected = jdbcTemplate.update(sql, parameters);

        if (rowsAffected > 0) {
            return id;
        } else {
            return -1;
        }
    }

    public Optional<Planet> getPlanet(long id) {
        String sql = "SELECT * FROM " + table + " WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Planet planet = jdbcTemplate.queryForObject(sql, parameters, BeanPropertyRowMapper.newInstance(Planet.class));
            return Optional.ofNullable(planet);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Planet> getAllPlanets() {
        String sql = "SELECT * FROM " + table;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Planet.class));
    }
}
