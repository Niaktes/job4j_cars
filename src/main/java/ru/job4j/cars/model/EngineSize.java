package ru.job4j.cars.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "engine_size")
@Data
public class EngineSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private float size;

}