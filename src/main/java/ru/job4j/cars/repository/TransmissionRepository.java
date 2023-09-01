package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Transmission;

public interface TransmissionRepository {

    Collection<Transmission> findAll();

}