package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Category;

public interface CategoryRepository {

    Collection<Category> findAll();

}