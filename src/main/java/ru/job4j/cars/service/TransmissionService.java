package ru.job4j.cars.service;

import java.util.Collection;
import ru.job4j.cars.model.Transmission;

public interface TransmissionService {

    Collection<Transmission> findAll();

}