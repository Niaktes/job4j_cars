package ru.job4j.cars.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "car")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "BRAND_ID_FK"))
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id", foreignKey = @ForeignKey(name = "MODEL_ID_FK"))
    private Model model;

    @ManyToOne
    @JoinColumn(name = "body_id", foreignKey = @ForeignKey(name = "BODY_ID_FK"))
    private Body body;

    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    @ManyToOne
    @JoinColumn(name = "transmission_id", foreignKey = @ForeignKey(name = "TRANSMISSION_ID_FK"))
    private Transmission transmission;

    @ManyToOne
    @JoinColumn(name = "color_id", foreignKey = @ForeignKey(name = "COLOR_ID_FK"))
    private Color color;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "CATEGORY_ID_FK"))
    private Category category;

    private int year;

    private int mileage;

    @EqualsAndHashCode.Include
    private String vin;

}