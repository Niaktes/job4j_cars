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
     * @return Optional user c ID, или пустой Optional при совпадении уникальных полей.
     */
    public Optional<User> create(User user) {
        Optional<User> result = Optional.empty();
        if (crudRepository.run(session -> session.persist(user))) {
            result = Optional.of(user);
        }
        return result;
    }

    /**
     * Обновить в базе данных пользователя.
     * @param user пользователь.
     * @return true в случае удачного обновления.
     */
    public boolean update(User user) {
        return crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID.
     */
    public void delete(int userId) {
        crudRepository.run("DELETE FROM User WHERE id = :uId", Map.of("uId", userId));
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
                "FROM User WHERE id = :uId",
                User.class,
                Map.of("uId", userId)
        );
    }

    /**
     * Список пользователей по Login LIKE %key%.
     * @param key key.
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User WHERE login LIKE :uKey",
                User.class,
                Map.of("uKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "FROM User WHERE login = :uLogin",
                User.class,
                Map.of("uLogin", login)
        );
    }

}