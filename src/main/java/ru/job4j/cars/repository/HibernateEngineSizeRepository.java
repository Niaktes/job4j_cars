package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.EngineSize;

@Repository
@AllArgsConstructor
public class HibernateEngineSizeRepository implements EngineSizeRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все объёмы двигателя.
     * @return список объёмов двигателя.
     */
    @Override
    public Collection<EngineSize> findAll() {
        return crudRepository.query("FROM EngineSize", EngineSize.class);
    }

}