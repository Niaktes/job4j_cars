package ru.job4j.cars.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.EngineRepository;

@Service
@AllArgsConstructor
public class SimpleEngineService implements EngineService {

    private final EngineRepository engineRepository;

    @Override
    public Engine save(Engine engine) {
        return engineRepository.save(engine).get();
    }

    @Override
    public Optional<Engine> findByFuelTypeAndSize(FuelType fuelType, EngineSize size) {
        return engineRepository.findByFuelTypeAndSize(fuelType, size);
    }

}