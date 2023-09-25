package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.ColorRepository;

@Service
@AllArgsConstructor
public class SimpleColorService implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public Collection<Color> findAll() {
        return colorRepository.findAll();
    }

}