package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Brand;

public interface BrandService {

    Collection<Brand> findAll();

    Brand getById(int id);

}