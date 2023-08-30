package ru.job4j.cars.repository;

import java.util.Collection;
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
     * Найти в БД все типы двигателей.
     * @return список двигателей.
     */
    public Collection<Engine> findAll() {
        return crudRepository.query("FROM Engine", Engine.class);
    }

    /**
     * Обновить в базе данных двигатель.
     * @param engine двигатель.
     * @return true в случае удачного обновления.
     */
    public boolean update(Engine engine) {
        return crudRepository.run(session -> session.update(engine));
    }

    /**
     * Удалить двигатель по id.
     * @param engineId ID.
     */
    public void delete(int engineId) {
        crudRepository.run("DELETE FROM Engine WHERE id = :eId", Map.of("eId", engineId));
    }

}