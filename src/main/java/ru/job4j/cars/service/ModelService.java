package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Model;

public interface ModelService {

    Collection<Model> findAll();

}