package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.FuelType;

@Repository
@AllArgsConstructor
public class HibernateFuelTypeRepository implements FuelTypeRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все типы топлива.
     * @return список типов топлива.
     */
    @Override
    public Collection<FuelType> findAll() {
        return crudRepository.query("FROM FuelType", FuelType.class);
    }

}