package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

public interface PostService {

    Optional<Post> save(Post post, Set<ImageDto> imagesDto);

    Optional<Post> findById(int id);

    List<Post> findAllNotSold();

    List<Post> findAllByUserId(int id);

    boolean update(Post post, Set<ImageDto> imagesDto);

    void delete(Post post);

    List<Post> findAllByCriteria(Car car, boolean imagesExist, int createdDaysBefore, long minPrice,
                                 long maxPrice);

}