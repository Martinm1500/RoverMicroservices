package com.microservice.obstacle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name="obstacles")
public class Obstacle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int x;

    @NotNull
    private int y;

    @NotNull
    private Long planetId;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
