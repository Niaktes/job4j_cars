package ru.job4j.cars.service;

import java.util.Optional;
import ru.job4j.cars.model.User;

public interface UserService {

    Optional<User> save(User user);

    boolean update(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmailAndPassword(String email, String password);

}