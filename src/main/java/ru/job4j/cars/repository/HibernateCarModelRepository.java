package ru.job4j.cars.repository;

import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarModel;

@Repository
@AllArgsConstructor
public class HibernateCarModelRepository implements CarModelRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все модели автомобилей по ID бренда.
     * @param id ID бренда
     * @return список моделей.
     */
    @Override
    public Collection<CarModel> findAllByBrandId(int id) {
        return crudRepository.query("FROM CarModel WHERE brand_id = :bId",
                CarModel.class, Map.of("bId", id));
    }

}