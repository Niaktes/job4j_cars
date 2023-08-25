package ru.job4j.cars.repository;

import java.util.Map;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Owner;

@AllArgsConstructor
public class OwnerRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param owner владелец.
     * @return владелец с id.
     */
    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    /**
     * Обновить в базе данных владельца.
     * @param owner владелец.
     * @return true в случае удачного обновления.
     */
    public boolean update(Owner owner) {
        return crudRepository.run(session -> session.merge(owner));
    }

    /**
     * Удалить владельца по id.
     * @param ownerId ID.
     * @return true в случае удачного удаления.
     */
    public boolean delete(int ownerId) {
        return crudRepository.run("DELETE FROM Owner WHERE id = :oId", Map.of("oId", ownerId));
    }

}