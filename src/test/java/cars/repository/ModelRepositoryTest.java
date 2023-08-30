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
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.ModelRepository;

class ModelRepositoryTest {

    private static StandardServiceRegistry registry;
    private static ModelRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new ModelRepository(new CrudRepository(sf));
    }

    @AfterEach
    void clearTable() {
        repository.findAll().forEach(el -> repository.delete(el.getId()));
    }

    @AfterAll
    static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenNothingAddedThenFindAllEmptyCollection() {
        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void whenAddThenFindAllCollection() {
        CarModel first = new CarModel();
        CarModel second = new CarModel();
        first.setName("first");
        first.setBrandName("first");
        second.setName("second");
        second.setBrandName("second");

        repository.create(first);
        repository.create(second);

        assertEquals(List.of(first, second), repository.findAll());
    }

    @Test
    void whenAddAndUpdateThenFindUpdated() {
        CarModel first = new CarModel();
        CarModel second = new CarModel();
        first.setName("first");
        first.setBrandName("first");
        second.setName("second");
        second.setBrandName("second");

        repository.create(first);
        second.setId(first.getId());
        repository.update(second);

        assertEquals(List.of(second), repository.findAll());
    }

    @Test
    void whenUpdateWrongEntityThenGetFalse() {
        CarModel first = new CarModel();
        CarModel second = new CarModel();
        first.setName("first");
        first.setBrandName("first");
        second.setName("second");
        second.setBrandName("second");

        repository.create(first);

        assertFalse(repository.update(second));
    }

    @Test
    void whenAddAndDeleteThenFindCollectionWithoutDeleted() {
        CarModel first = new CarModel();
        CarModel second = new CarModel();
        CarModel third = new CarModel();
        first.setName("first");
        first.setBrandName("first");
        second.setName("second");
        second.setBrandName("second");
        third.setName("third");
        third.setBrandName("third");

        repository.create(first);
        repository.create(second);
        repository.create(third);
        repository.delete(second.getId());

        assertEquals(List.of(first, third), repository.findAll());
    }

}