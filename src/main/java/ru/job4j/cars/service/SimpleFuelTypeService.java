package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.FuelTypeRepository;

@Service
@AllArgsConstructor
public class SimpleFuelTypeService implements FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    @Override
    public Collection<FuelType> findAll() {
        return fuelTypeRepository.findAll();
    }

}