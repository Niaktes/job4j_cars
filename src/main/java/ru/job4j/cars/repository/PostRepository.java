package ru.job4j.cars.repository;

import java.util.List;
import java.util.Optional;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

public interface PostRepository {

    Optional<Post> save(Post post);

    Optional<Post> findById(int id);

    List<Post> findAllNotSold();

    List<Post> findAllByUserId(int id);

    boolean update(Post post);

    void delete(Post post);

    void deleteAllByUser(User user);

    List<Post> findAllByCriteria(Car car, boolean imagesExist, int createdDaysBefore, long minPrice,
                                 long maxPrice);

}