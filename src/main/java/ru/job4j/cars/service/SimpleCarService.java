package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CarRepository;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {

    private final CarRepository carRepository;

    @Override
    public void delete(Car car) {
        carRepository.delete(car);
    }

}