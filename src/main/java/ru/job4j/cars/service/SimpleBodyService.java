package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.BodyRepository;

@Service
@AllArgsConstructor
public class SimpleBodyService implements BodyService {

    private final BodyRepository bodyRepository;

    @Override
    public Collection<Body> findAll() {
        return bodyRepository.findAll();
    }

}