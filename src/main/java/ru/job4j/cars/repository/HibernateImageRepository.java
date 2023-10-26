package ru.job4j.cars.repository;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Image;

@Repository
@AllArgsConstructor
public class HibernateImageRepository implements ImageRepository {

    private static final String DEFAULT_PHOTO_NAME = "defaultPhoto.png";

    private final CrudRepository crudRepository;

    /**
     * Получить из БД стандартное изображение.
     * @return изображение.
     */
    @Override
    public Image getDefaultImage() {
        return crudRepository.one(
                "FROM Image WHERE name = :iName",
                Image.class,
                Map.of("iName", DEFAULT_PHOTO_NAME)
        );
    }

    /**
     * Получить из БД изображение по id.
     * @param id ID изображения.
     * @return изображение.
     */
    @Override
    public Optional<Image> findById(int id) {
        return crudRepository.optional("FROM Image WHERE id = :Id",
                Image.class,
                Map.of("Id", id));
    }

}