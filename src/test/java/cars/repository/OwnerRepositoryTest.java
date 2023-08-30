package cars.repository;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.OwnerRepository;
import ru.job4j.cars.repository.UserRepository;

class OwnerRepositoryTest {

    private static StandardServiceRegistry registry;
    private static OwnerRepository ownerRepository;
    private static UserRepository userRepository;
    private static User user;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        ownerRepository = new OwnerRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);

        user = new User();
        user.setLogin("test");
        user.setPassword("test");

        userRepository.create(user);
    }

    @AfterEach
    void clearTable() {
        ownerRepository.findAll().forEach(el -> ownerRepository.delete(el.getId()));
    }

    @AfterAll
    static void cleanAndClose() {
        userRepository.delete(user.getId());
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenNothingAddedThenFindAllEmptyCollection() {
        assertEquals(List.of(), ownerRepository.findAll());
    }

    @Test
    void whenAddThenFindAllCollection() {
        Owner first = new Owner();
        Owner second = new Owner();
        first.setName("first");
        first.setUser(user);
        second.setName("second");
        second.setUser(user);

        ownerRepository.create(first);
        ownerRepository.create(second);

        assertEquals(List.of(first, second), ownerRepository.findAll());
    }

    @Test
    void whenAddAndUpdateThenFindUpdated() {
        Owner first = new Owner();
        Owner second = new Owner();
        first.setName("first");
        first.setUser(user);
        second.setName("second");
        second.setUser(user);

        ownerRepository.create(first);
        second.setId(first.getId());
        ownerRepository.update(second);

        assertEquals(List.of(second), ownerRepository.findAll());
    }

    @Test
    void whenUpdateWrongEntityThenGetFalse() {
        Owner first = new Owner();
        Owner second = new Owner();
        first.setName("first");
        first.setUser(user);
        second.setName("second");
        second.setUser(user);

        ownerRepository.create(first);

        assertFalse(ownerRepository.update(second));
    }

    @Test
    void whenAddAndDeleteThenFindCollectionWithoutDeleted() {
        Owner first = new Owner();
        Owner second = new Owner();
        Owner third = new Owner();
        first.setName("first");
        second.setName("second");
        third.setName("third");
        first.setUser(user);
        second.setUser(user);
        third.setUser(user);

        ownerRepository.create(first);
        ownerRepository.create(second);
        ownerRepository.create(third);
        ownerRepository.delete(second.getId());

        assertEquals(List.of(first, third), ownerRepository.findAll());
    }

}