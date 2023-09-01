package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Color;

public interface ColorRepository {

    Collection<Color> findAll();

}