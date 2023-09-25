package ru.job4j.cars.service;

import java.util.Optional;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;

public interface EngineService {

    Optional<Engine> findByFuelTypeAndSize(FuelType fuelType, EngineSize size);

}