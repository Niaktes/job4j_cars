package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;

@Repository
@AllArgsConstructor
public class HibernateBodyRepository implements BodyRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все типы кузовов.
     * @return список кузовов.
     */
    @Override
    public Collection<Body> findAll() {
        return crudRepository.query("FROM Body", Body.class);
    }

}