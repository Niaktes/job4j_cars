package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.CarModel;

public interface CarModelRepository {

    Collection<CarModel> findAllByBrandId(int id);

    CarModel getById(int id);

}