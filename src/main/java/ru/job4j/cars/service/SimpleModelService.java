package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Model;
import ru.job4j.cars.repository.ModelRepository;

@Service
@AllArgsConstructor
public class SimpleModelService implements ModelService {

    private final ModelRepository modelRepository;

    @Override
    public Collection<Model> findAll() {
        return modelRepository.findAll();
    }

}