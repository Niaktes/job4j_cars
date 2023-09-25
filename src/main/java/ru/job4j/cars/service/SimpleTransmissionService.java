package ru.job4j.cars.service;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Transmission;
import ru.job4j.cars.repository.TransmissionRepository;

@Service
@AllArgsConstructor
public class SimpleTransmissionService implements TransmissionService {

    private final TransmissionRepository transmissionRepository;

    @Override
    public Collection<Transmission> findAll() {
        return transmissionRepository.findAll();
    }

}