package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.FuelType;

public interface FuelTypeService {

    Collection<FuelType> findAll();

}