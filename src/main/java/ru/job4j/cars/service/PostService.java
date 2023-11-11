package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

public interface PostService {

    Optional<Post> save(Post post, ImageDto imageDto);

    Optional<Post> findById(int id);

    List<Post> findAllNotSold();

    List<Post> findAllByUserId(int id);

    boolean update(Post post, ImageDto imageDto);

    void deleteAllByUser(User user);

    List<Post> findAllByCriteria(PostSearchDto searchDto);

}