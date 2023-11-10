package ru.job4j.cars.repository;

import java.util.Optional;
import ru.job4j.cars.model.Image;

public interface ImageRepository {

    Image getDefaultImage();

    Optional<Image> findById(int id);

    void delete(Image image);

}