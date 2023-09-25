package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Body;

public interface BodyService {

    Collection<Body> findAll();

}