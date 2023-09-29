package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarModel;

@Repository
@AllArgsConstructor
public class HibernateCarModelRepository implements CarModelRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все модели автомобилей.
     * @return список моделей.
     */
    @Override
    public Collection<CarModel> findAll() {
        return crudRepository.query("FROM CarModel", CarModel.class);
    }

}