package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Model;

public interface ModelRepository {

    Collection<Model> findAll();

}