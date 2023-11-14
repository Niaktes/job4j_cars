package cars.repository;

import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Image;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateImageRepository;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateImageRepositoryTest {

    private static StandardServiceRegistry registry;
    private static CrudRepository crudRepository;
    private static HibernateTestUtility testUtil;
    private static HibernateImageRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernateImageRepository(crudRepository);
    }

    @AfterAll
    static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @AfterEach
    void clearTable() {
        testUtil.deleteAll(crudRepository.query("FROM Image", Image.class));
    }

    @Test
    void whenGetDefaultImageThenGetImage() {
        Image actual = new Image();
        actual.setName("defaultPhoto.png");
        actual.setPath("path");
        Image expected = new Image();
        expected.setName("defaultPhoto.png");
        expected.setPath("path");

        testUtil.save(actual);
        expected.setId(actual.getId());

        assertThat(repository.getDefaultImage()).isEqualTo(expected);
    }

    @Test
    void whenFindByIdThenGetOptionalImage() {
        Image image = new Image();
        image.setName("test");
        image.setPath("test");

        testUtil.save(image);

        assertThat(repository.findById(image.getId())).contains(image);
    }

    @Test
    void whenFindByWrongIdThenGetOptionalEmpty() {
        Image image = new Image();
        image.setName("test");
        image.setPath("test");

        testUtil.save(image);
        int wrongId = image.getId() + 1;

        assertThat(repository.findById(wrongId)).isEmpty();
    }

    @Test
    void whenDeleteAndFindByIdThenGetOptionalEmpty() {
        Image image = new Image();
        image.setName("test");
        image.setPath("test");

        testUtil.save(image);

        assertThat(repository.findById(image.getId())).isNotEmpty();
        repository.delete(image);
        assertThat(repository.findById(image.getId())).isEmpty();
    }

}