package ru.job4j.cars.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @EqualsAndHashCode.Include
    private String email;

    @EqualsAndHashCode.Include
    private String password;

    private String name;

    private String phone;

}