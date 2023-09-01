package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.EngineSize;

public interface EngineSizeRepository {

    Collection<EngineSize> findAll();

}