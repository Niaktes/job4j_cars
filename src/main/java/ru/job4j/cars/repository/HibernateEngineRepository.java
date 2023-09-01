package ru.job4j.cars.repository;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;

@Repository
@AllArgsConstructor
public class HibernateEngineRepository implements EngineRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД двигатель по типу топлива и объёму двигателя.
     * @param fuelType тип топлива.
     * @param engineSize объем двигателя.
     * @return Optional двигателя.
     */
    @Override
    public Optional<Engine> findByFuelTypeAndSize(FuelType fuelType, EngineSize engineSize) {
        return crudRepository.optional(
                "FROM Engine AS e WHERE e.fuelType = :ft AND e.engineSize = :es",
                Engine.class,
                Map.of("ft", fuelType, "es", engineSize)
        );
    }

}