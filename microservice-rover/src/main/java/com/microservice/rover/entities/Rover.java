package com.microservice.rover.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Rover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int x;

    @NotNull
    private int y;

    @NotNull
    private char orientation;

    @NotNull
    private Long planetId;

    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';

    public Rover(int x, int y, char orientation, Long planetId) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.planetId = planetId;
    }

    public Rover(){}

    public static boolean isValidOrientation(char orientation){
        return orientation == NORTH || orientation == SOUTH || orientation == EAST || orientation == WEST;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rover rover)) return false;
        return x == rover.x && y == rover.y && orientation == rover.orientation && Objects.equals(id, rover.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, orientation);
    }

    @Override
    public String toString() {
        return "Rover{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                '}';
    }
}
