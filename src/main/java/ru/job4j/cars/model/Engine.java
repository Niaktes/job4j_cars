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

    @ManyToOne
    @JoinColumn(name = "fuel_type_id", foreignKey = @ForeignKey(name = "FUEL_TYPE_ID_FK"))
    private FuelType fuelType;

    @ManyToOne
    @JoinColumn(name = "engine_size_id", foreignKey = @ForeignKey(name = "ENGINE_SIZE_ID_FK"))
    private EngineSize engineSize;

}
