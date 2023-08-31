package ru.job4j.cars.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "body")
@Data
public class Body {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

}