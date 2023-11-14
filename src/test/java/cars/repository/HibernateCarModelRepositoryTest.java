package cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateCarModelRepository;

class HibernateCarModelRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernateCarModelRepository repository;
    private static Brand brand;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateCarModelRepository(crudRepository);
        brand = new Brand();
        brand.setName("brand");
        testUtil.save(brand);
    }

    @AfterEach
    void clearTable() {
        testUtil.deleteAll(repository.findAll());
    }

    @AfterAll
    static void clearAndClose() {
        testUtil.clearDatabase();
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenDBIsEmptyThenFindAllEmptyCollection() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void whenFindAllBrandsThenGetCollectionOfBrands() {
        CarModel first = new CarModel();
        first.setName("first");
        first.setBrandId(brand.getId());
        CarModel second = new CarModel();
        second.setName("second");
        second.setBrandId(brand.getId());
        CarModel expectedFirst = new CarModel();
        expectedFirst.setName("first");
        expectedFirst.setBrandId(brand.getId());
        CarModel expectedSecond = new CarModel();
        expectedSecond.setName("second");
        expectedSecond.setBrandId(brand.getId());

        testUtil.save(first);
        testUtil.save(second);
        expectedFirst.setId(first.getId());
        expectedSecond.setId(second.getId());

        assertThat(repository.findAll()).containsExactly(expectedFirst, expectedSecond);
    }

    @Test
    void whenGetByIdThenGetCarModel() {
        CarModel actual = new CarModel();
        actual.setName("model");
        actual.setBrandId(brand.getId());
        CarModel expected = new CarModel();
        expected.setName("model");
        expected.setBrandId(brand.getId());

        testUtil.save(actual);
        expected.setId(actual.getId());

        assertThat(repository.getById(actual.getId())).isEqualTo(expected);
    }

}