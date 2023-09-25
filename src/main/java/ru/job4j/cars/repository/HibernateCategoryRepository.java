package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Category;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;
    /**
     * Получить из БД все типы категорий.
     * @return список категорий.
     */
    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

}