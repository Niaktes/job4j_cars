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
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;
import ru.job4j.cars.repository.ModelRepository;

class CarRepositoryTest {

    private static StandardServiceRegistry registry;
    private static EngineRepository engineRepository;
    private static ModelRepository modelRepository;
    private static CarRepository carRepository;
    private static Engine engine;
    private static CarModel model;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        engineRepository = new EngineRepository(crudRepository);
        modelRepository = new ModelRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);

        engine = new Engine();
        engine.setName("engine");
        model = new CarModel();
        model.setName("model");
        model.setBrandName("modelBrand");

        engineRepository.create(engine);
        modelRepository.create(model);
    }

    @AfterEach
    void clearTable() {
        carRepository.findAll().forEach(el -> carRepository.delete(el.getId()));
    }

    @AfterAll
    static void cleanAndClose() {
        engineRepository.delete(engine.getId());
        modelRepository.delete(model.getId());
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenNothingAddedThenFindAllEmptyCollection() {
        assertEquals(List.of(), carRepository.findAll());
    }

    @Test
    void whenAddThenFindAllCollection() {
        Car first = new Car();
        Car second = new Car();
        first.setName("first");
        first.setEngine(engine);
        first.setCarModel(model);
        second.setName("second");
        second.setEngine(engine);
        second.setCarModel(model);

        carRepository.create(first);
        carRepository.create(second);

        assertEquals(List.of(first, second), carRepository.findAll());
    }

    @Test
    void whenAddAndUpdateThenFindUpdated() {
        Car first = new Car();
        Car second = new Car();
        first.setName("first");
        first.setEngine(engine);
        first.setCarModel(model);
        second.setName("second");
        second.setEngine(engine);
        second.setCarModel(model);

        carRepository.create(first);
        second.setId(first.getId());
        carRepository.update(second);

        assertEquals(List.of(second), carRepository.findAll());
    }

    @Test
    void whenUpdateWrongEntityThenGetFalse() {
        Car first = new Car();
        Car second = new Car();
        first.setName("first");
        first.setEngine(engine);
        first.setCarModel(model);
        second.setName("second");
        second.setEngine(engine);
        second.setCarModel(model);

        carRepository.create(first);

        assertFalse(carRepository.update(second));
    }

    @Test
    void whenAddAndDeleteThenFindCollectionWithoutDeleted() {
        Car first = new Car();
        Car second = new Car();
        Car third = new Car();
        first.setName("first");
        first.setEngine(engine);
        first.setCarModel(model);
        second.setName("second");
        second.setEngine(engine);
        second.setCarModel(model);
        third.setName("third");
        third.setEngine(engine);
        third.setCarModel(model);

        carRepository.create(first);
        carRepository.create(second);
        carRepository.create(third);
        carRepository.delete(second.getId());

        assertEquals(List.of(first, third), carRepository.findAll());
    }

}