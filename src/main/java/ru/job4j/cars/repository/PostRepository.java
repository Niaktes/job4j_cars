package ru.job4j.cars.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;

@AllArgsConstructor
public class PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param post пост.
     * @return пост с id.
     */
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    /**
     * Найти пост по ID.
     * @param id ID поста.
     * @return Optional поста.
     */
    public Optional<Post> findById(int id) {
        return crudRepository.optional("FROM Post WHERE id = :pId", Post.class, Map.of("pId", id));
    }

    /**
     * Найти все посты конкретного пользователя.
     * @param id ID пользователя
     * @return список постов.
     */
    public List<Post> findAllByUserId(int id) {
        return crudRepository.query("FROM Post WHERE auto_user_id = :uId",
                Post.class,
                Map.of("uId", id));
    }

    /**
     * Обновить в базе данных пост.
     * @param post пост.
     * @return true в случае удачного обновления.
     */
    public boolean update(Post post) {
        return crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить пост по id.
     * @param postId ID.
     * @return true в случае удачного удаления.
     */
    public boolean delete(int postId) {
        return crudRepository.run("DELETE FROM Post WHERE id = :pId", Map.of("pId", postId));
    }

}