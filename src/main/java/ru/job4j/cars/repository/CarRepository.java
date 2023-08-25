package ru.job4j.cars.repository;

import java.util.Map;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;

@AllArgsConstructor
public class CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    /**
     * Обновить в базе данных автомобиль.
     * @param car автомобиль.
     * @return true в случае удачного обновления.
     */
    public boolean update(Car car) {
        return crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить автомобиль по id.
     * @param carId ID.
     * @return true в случае удачного удаления.
     */
    public boolean delete(int carId) {
        return crudRepository.run("DELETE FROM Car WHERE id = :cId", Map.of("cId", carId));
    }

}