package cars.repository;

import java.util.Collection;
import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateCarRepository;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateCarRepositoryTest {

    private static StandardServiceRegistry registry;
    private static CrudRepository crudRepository;
    private static HibernateTestUtility testUtil;
    private static HibernateCarRepository repository;
    private static Car car;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateCarRepository(crudRepository);
        car = testUtil.addCarToDatabase();
    }

    @AfterAll
    static void clearAndClose() {
        testUtil.clearDatabase();
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenDeleteCarThenDatabaseIsEmpty() {
        Collection<Car> beforeDeleteCars = crudRepository.query("FROM Car", Car.class);
        assertThat(beforeDeleteCars).isNotEmpty();

        repository.delete(car);

        Collection<Car> afterDeleteCars = crudRepository.query("FROM Car", Car.class);
        assertThat(afterDeleteCars).isEmpty();
    }

}