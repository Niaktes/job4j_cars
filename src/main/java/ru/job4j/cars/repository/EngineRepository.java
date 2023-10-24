package ru.job4j.cars.repository;

import java.util.Optional;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;

public interface EngineRepository {

    Optional<Engine> save(Engine engine);

    Optional<Engine> findByFuelTypeAndSize(FuelType fuelType, EngineSize size);

}