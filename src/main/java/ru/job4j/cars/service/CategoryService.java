package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Category;

public interface CategoryService {

    Collection<Category> findAll();

}