package ru.job4j.cars.repository;

import java.util.Collection;
import ru.job4j.cars.model.Image;

public interface ImageRepository {

    Image getDefaultImage();

    Collection<Image> getImagesByPostId(int id);

}