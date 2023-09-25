package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Color;

public interface ColorService {

    Collection<Color> findAll();

}