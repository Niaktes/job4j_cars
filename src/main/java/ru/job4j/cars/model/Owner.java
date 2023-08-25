package ru.job4j.cars.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "owner")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @EqualsAndHashCode.Include
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ownership_history", joinColumns = {
            @JoinColumn(name = "owner_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
            @JoinColumn(name = "car_id", nullable = false, updatable = false)
    })
    private List<Car> cars = new ArrayList<>();

}