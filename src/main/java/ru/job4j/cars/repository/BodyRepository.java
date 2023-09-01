package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Body;

public interface BodyRepository {

    Collection<Body> findAll();

}