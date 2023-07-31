package ru.job4j.cars.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.User;

@AllArgsConstructor
public class UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Обновить в базе данных пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID.
     */
    public void delete(int userId) {
        crudRepository.run("DELETE FROM User WHERE id = :fId", Map.of("fId", userId));
    }

    /**
     * Список пользователей, отсортированных по ID.
     * @return список пользователей.
     */
    public List<User> findAllOrderedById() {
        return crudRepository.query("FROM User ORDER BY id ASC", User.class);
    }

    /**
     * Найти пользователя по ID.
     * @param userId ID.
     * @return Optional or user.
     */
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "FROM User WHERE id = :fId",
                User.class,
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователей по Login LIKE %key%.
     * @param key key.
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User WHERE login LIKE :fKey",
                User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "FROM User WHERE login = :fLogin",
                User.class,
                Map.of("fLogin", login)
        );
    }

}