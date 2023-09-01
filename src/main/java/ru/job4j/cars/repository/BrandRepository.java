package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Brand;

public interface BrandRepository {

    Collection<Brand> findAll();

}