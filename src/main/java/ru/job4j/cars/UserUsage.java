package ru.job4j.cars;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.UserRepository;

public class UserUsage {

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            CrudRepository crudRepository = new CrudRepository(sf);
            UserRepository userRepository = new UserRepository(crudRepository);
            User user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            User savedUser = userRepository.create(user).get();
            System.out.println(savedUser);
            userRepository.findAllOrderedById().forEach(System.out::println);
            userRepository.findByLikeLogin("e").forEach(System.out::println);
            userRepository.findById(user.getId()).ifPresent(System.out::println);
            userRepository.findByLogin("admin").ifPresent(System.out::println);
            user.setPassword("password");
            userRepository.update(user);
            userRepository.findById(user.getId()).ifPresent(System.out::println);
            userRepository.delete(user.getId());
            userRepository.findAllOrderedById().forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}