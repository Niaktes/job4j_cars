package cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateEngineRepository;

class HibernateEngineRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernateEngineRepository repository;
    private static FuelType fuelType;
    private static EngineSize engineSize;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateEngineRepository(crudRepository);
        fuelType = new FuelType();
        fuelType.setName("fuelType");
        engineSize = new EngineSize();
        engineSize.setSize(1.1f);
        testUtil.save(fuelType);
        testUtil.save(engineSize);
    }

    @AfterAll
    static void clearAndClose() {
        testUtil.clearDatabase();
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @AfterEach
    void clearTable() {
        repository.findByFuelTypeAndSize(fuelType, engineSize).ifPresent(testUtil::delete);
    }

    @Test
    void whenEngineWasNotSavedThenGetOptionalEmpty() {
        assertThat(repository.findByFuelTypeAndSize(fuelType, engineSize)).isEmpty();
    }

    @Test
    void whenEngineWasSavedThenGetEngineOptional() {
        Engine actual = new Engine();
        actual.setFuelType(fuelType);
        actual.setEngineSize(engineSize);
        Engine expected = new Engine();
        expected.setFuelType(fuelType);
        expected.setEngineSize(engineSize);

        testUtil.save(actual);
        expected.setId(actual.getId());

        assertThat(repository.findByFuelTypeAndSize(fuelType, engineSize)).isPresent();
        assertThat(repository.findByFuelTypeAndSize(fuelType, engineSize)).contains(expected);
    }

}