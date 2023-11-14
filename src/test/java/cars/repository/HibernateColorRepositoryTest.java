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
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateColorRepository;

class HibernateColorRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernateColorRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateColorRepository(crudRepository);
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
        Color first = new Color();
        first.setName("first");
        Color second = new Color();
        second.setName("second");
        Color expectedFirst = new Color();
        expectedFirst.setName("first");
        Color expectedSecond = new Color();
        expectedSecond.setName("second");

        testUtil.save(first);
        testUtil.save(second);
        expectedFirst.setId(first.getId());
        expectedSecond.setId(second.getId());

        assertThat(repository.findAll()).containsExactly(expectedFirst, expectedSecond);
    }

}