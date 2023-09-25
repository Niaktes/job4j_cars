package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.repository.EngineSizeRepository;

@Service
@AllArgsConstructor
public class SimpleEngineSizeService implements EngineSizeService {

    private final EngineSizeRepository engineSizeRepository;

    @Override
    public Collection<EngineSize> findAll() {
        return engineSizeRepository.findAll();
    }

}