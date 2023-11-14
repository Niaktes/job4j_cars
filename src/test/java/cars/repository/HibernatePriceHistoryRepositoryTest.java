package cars.repository;

import java.time.LocalDateTime;
import java.util.Set;
import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernatePriceHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;

class HibernatePriceHistoryRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernatePriceHistoryRepository repository;
    private static Post post;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernatePriceHistoryRepository(crudRepository);

        User user = testUtil.addUserToDatabase();
        Car car = testUtil.addCarToDatabase();
        post = testUtil.addPostToDatabase(user, car);
    }

    @AfterAll
    static void clearAndClose() {
        testUtil.clearDatabase();
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenGetPriceHistoryThenGetSetOfPriceHistory() {
        PriceHistory first = new PriceHistory();
        first.setPrice(1L);
        first.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        PriceHistory second = new PriceHistory();
        second.setPrice(2L);
        second.setDate(LocalDateTime.of(2, 2, 2, 2, 2));

        post.setPriceHistories(Set.of(first, second));
        testUtil.save(post);

        assertThat(repository.getPriceHistoriesByPostId(post.getId())).contains(first, second);
    }

    @Test
    void whenGetPriceHistoryByWrongPostIdThenGetEmptySet() {
        PriceHistory first = new PriceHistory();
        first.setPrice(1L);
        first.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        PriceHistory second = new PriceHistory();
        second.setPrice(2L);
        second.setDate(LocalDateTime.of(2, 2, 2, 2, 2));

        post.setPriceHistories(Set.of(first, second));
        testUtil.save(post);

        assertThat(repository.getPriceHistoriesByPostId(post.getId() + 1)).isEmpty();
    }

}