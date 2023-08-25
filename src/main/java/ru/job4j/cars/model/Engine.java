package ru.job4j.cars.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "engine")
@Data
public class Engine {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    private String name;

}
