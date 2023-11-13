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
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateBrandRepository;

class HibernateBrandRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernateBrandRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateBrandRepository(crudRepository);
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
        Brand first = new Brand();
        first.setName("first");
        Brand second = new Brand();
        second.setName("second");
        Brand expectedFirst = new Brand();
        expectedFirst.setName("first");
        Brand expectedSecond = new Brand();
        expectedSecond.setName("second");

        testUtil.save(first);
        testUtil.save(second);
        expectedFirst.setId(first.getId());
        expectedSecond.setId(second.getId());

        assertThat(repository.findAll()).containsExactly(expectedFirst, expectedSecond);
    }

    @Test
    void whenGetByIdThenGetBrand() {
        Brand actual = new Brand();
        actual.setName("brand");
        Brand expected = new Brand();
        expected.setName("brand");

        testUtil.save(actual);
        expected.setId(actual.getId());

        assertThat(repository.getById(actual.getId())).isEqualTo(expected);
    }

}