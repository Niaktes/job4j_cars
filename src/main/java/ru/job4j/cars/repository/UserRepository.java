package ru.job4j.cars.repository;

import java.util.Optional;
import ru.job4j.cars.model.User;

public interface UserRepository {

    Optional<User> save(User user);

    boolean update(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmailAndPassword(String email, String password);

}