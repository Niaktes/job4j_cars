package ru.job4j.cars.repository;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;

@Repository
@AllArgsConstructor
public class HibernateBrandRepository implements BrandRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все бренды.
     * @return список брендов
     */
    @Override
    public Collection<Brand> findAll() {
        return crudRepository.query("FROM Brand", Brand.class);
    }
}
