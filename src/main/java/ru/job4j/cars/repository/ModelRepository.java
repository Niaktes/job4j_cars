package ru.job4j.cars.repository;

import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.CarModel;

@AllArgsConstructor
public class ModelRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param carModel модель автомобиля.
     * @return модель с id.
     */
    public CarModel create(CarModel carModel) {
        crudRepository.run(session -> session.persist(carModel));
        return carModel;
    }

    /**
     * Найти в БД все модели автомобилей.
     * @return список моделей.
     */
    public Collection<CarModel> findAll() {
        return crudRepository.query("FROM CarModel", CarModel.class);
    }

    /**
     * Обновить в базе данных модель автомобиля.
     * @param carModel модель автомобиля.
     * @return true в случае удачного обновления.
     */
    public boolean update(CarModel carModel) {
        return crudRepository.run(session -> session.update(carModel));
    }

    /**
     * Удалить модель автомобиля по id.
     * @param modelId ID.
     */
    public void delete(int modelId) {
        crudRepository.run("DELETE FROM CarModel WHERE id = :mId", Map.of("mId", modelId));
    }

}