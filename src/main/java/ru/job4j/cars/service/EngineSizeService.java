package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.EngineSize;

public interface EngineSizeService {

    Collection<EngineSize> findAll();

}