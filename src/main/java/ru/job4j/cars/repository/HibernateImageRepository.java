package ru.job4j.cars.repository;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Image;

@Repository
@AllArgsConstructor
public class HibernateImageRepository implements ImageRepository {

    private final CrudRepository crudRepository;

    @Override
    public Image getDefaultImage() {
        String defaultName = "default";
        return crudRepository.one(
                "FROM Image WHERE name = :iName",
                Image.class,
                Map.of("iName", defaultName)
        );
    }

}