package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.CarModel;

public interface CarModelService {

    Collection<CarModel> findAll();

}