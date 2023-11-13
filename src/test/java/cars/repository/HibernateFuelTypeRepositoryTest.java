package cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cars.testUtil.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateFuelTypeRepository;

class HibernateFuelTypeRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernateFuelTypeRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateFuelTypeRepository(crudRepository);
    }

    @AfterEach
    void clearTable() {
        testUtil.deleteAll(repository.findAll());
    }

    @AfterAll
    static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenDBIsEmptyThenFindAllEmptyCollection() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void whenFindAllBrandsThenGetCollectionOfBrands() {
        FuelType first = new FuelType();
        first.setName("first");
        FuelType second = new FuelType();
        second.setName("second");
        FuelType expectedFirst = new FuelType();
        expectedFirst.setName("first");
        FuelType expectedSecond = new FuelType();
        expectedSecond.setName("second");

        testUtil.save(first);
        testUtil.save(second);
        expectedFirst.setId(first.getId());
        expectedSecond.setId(second.getId());

        assertThat(repository.findAll()).containsExactly(expectedFirst, expectedSecond);
    }

}