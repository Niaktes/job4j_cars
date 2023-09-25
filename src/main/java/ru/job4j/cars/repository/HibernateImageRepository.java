package ru.job4j.cars.repository;

import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Image;

@Repository
@AllArgsConstructor
public class HibernateImageRepository implements ImageRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД стандартное изображение.
     * @return изображение.
     */
    @Override
    public Image getDefaultImage() {
        String defaultName = "default";
        return crudRepository.one(
                "FROM Image WHERE name = :iName",
                Image.class,
                Map.of("iName", defaultName)
        );
    }

    /**
     * Получить из БД все изображения конкретного поста.
     * @param id ID поста.
     * @return список изображений.
     */
    @Override
    public Collection<Image> getImagesByPostId(int id) {
        return crudRepository.query("FROM Image WHERE post_id = :pId;",
                Image.class,
                Map.of("pId", id));
    }

}