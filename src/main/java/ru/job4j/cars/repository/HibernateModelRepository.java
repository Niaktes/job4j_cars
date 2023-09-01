package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Model;

@Repository
@AllArgsConstructor
public class HibernateModelRepository implements ModelRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все модели автомобилей.
     * @return список моделей.
     */
    @Override
    public Collection<Model> findAll() {
        return crudRepository.query("FROM Model", Model.class);
    }

}