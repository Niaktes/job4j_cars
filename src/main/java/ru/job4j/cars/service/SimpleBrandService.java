package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.BrandRepository;

@Service
@AllArgsConstructor
public class SimpleBrandService implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public Collection<Brand> findAll() {
        return brandRepository.findAll();
    }

}