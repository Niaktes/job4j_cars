package ru.job4j.cars.repository;

import java.util.Collection;
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
     * Найти в БД всех владельцев.
     * @return список владельцев.
     */
    public Collection<Owner> findAll() {
        return crudRepository.query("FROM Owner", Owner.class);
    }

    /**
     * Обновить в базе данных владельца.
     * @param owner владелец.
     * @return true в случае удачного обновления.
     */
    public boolean update(Owner owner) {
        return crudRepository.run(session -> session.update(owner));
    }

    /**
     * Удалить владельца по id.
     * @param ownerId ID.
     */
    public void delete(int ownerId) {
        crudRepository.run("DELETE FROM Owner WHERE id = :oId", Map.of("oId", ownerId));
    }

}