package ru.job4j.cars.repository;

import java.util.Map;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Engine;

@AllArgsConstructor
public class EngineRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param engine двигатель.
     * @return двигатель с id.
     */
    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    /**
     * Обновить в базе данных двигатель.
     * @param engine двигатель.
     * @return true в случае удачного обновления.
     */
    public boolean update(Engine engine) {
        return crudRepository.run(session -> session.merge(engine));
    }

    /**
     * Удалить двигатель по id.
     * @param engineId ID.
     * @return true в случае удачного удаления.
     */
    public boolean delete(int engineId) {
        return crudRepository.run("DELETE FROM Engine WHERE id = :eId", Map.of("eId", engineId));
    }

}