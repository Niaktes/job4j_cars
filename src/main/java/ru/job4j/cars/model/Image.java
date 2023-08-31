package ru.job4j.cars.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "image")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @EqualsAndHashCode.Include
    private String path;

}