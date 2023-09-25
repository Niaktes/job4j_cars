package ru.job4j.cars.repository;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных пользователя.
     * @param user пользователь.
     * @return Optional user c ID, или пустой Optional при совпадении уникальных полей.
     */
    @Override
    public Optional<User> save(User user) {
        Optional<User> result = Optional.empty();
        if (crudRepository.run(session -> session.save(user))) {
            result = Optional.of(user);
        }
        return result;
    }

    /**
     * Обновить в базе данных пользователя.
     * @param user пользователь.
     * @return true в случае удачного обновления.
     */
    @Override
    public boolean update(User user) {
        return crudRepository.run(session -> session.update(user));
    }

    /**
     * Найти пользователя по email и паролю.
     * @param email email пользователя.
     * @param password пароль пользователя.
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional(
                "FROM User WHERE email = :uEmail AND password = :uPassword",
                User.class,
                Map.of("uEmail", email, "uPassword", password)
        );
    }

    /**
     * Удалить пользователя по email и паролю.
     * @param email email пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public void deleteByEmailAndPassword(String email, String password) {
        crudRepository.run("DELETE FROM User WHERE email = :uEmail AND password = :uPassword",
                Map.of("uEmail", email, "uPassword", password)
        );
    }

}