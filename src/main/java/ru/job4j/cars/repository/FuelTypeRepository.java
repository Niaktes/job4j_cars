package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.FuelType;

public interface FuelTypeRepository {

    Collection<FuelType> findAll();

}