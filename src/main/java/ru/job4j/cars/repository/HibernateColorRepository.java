package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Color;

@Repository
@AllArgsConstructor
public class HibernateColorRepository implements ColorRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Color> findAll() {
        return crudRepository.query("FROM Color", Color.class);
    }

}