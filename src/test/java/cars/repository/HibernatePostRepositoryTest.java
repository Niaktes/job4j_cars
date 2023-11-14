package cars.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import cars.util.HibernateTestUtility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernatePostRepository;

import static org.assertj.core.api.Assertions.assertThat;

class HibernatePostRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateTestUtility testUtil;
    private static HibernatePostRepository repository;
    private static User user;
    private static Car firstCar;
    private static Car secondCar;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        testUtil = new HibernateTestUtility(crudRepository);
        repository = new HibernatePostRepository(crudRepository);

        user = testUtil.addUserToDatabase();
        firstCar = testUtil.addCarToDatabase();
        secondCar = testUtil.addCarToDatabase();
    }

    @AfterAll
    static void clearAndClose() {
        testUtil.clearDatabase();
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @AfterEach
    void clearTable() {
        firstCar.setId(0);
        secondCar.setId(0);
        repository.deleteAllByUser(user);
    }

    @Test
    void whenSaveAndFindByIdThenGetPostOptional() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(post.getPrice());
        post.setPriceHistories(Set.of(priceHistory));

        Optional<Post> savedPost = repository.save(post);
        Optional<Post> foundPost = repository.findById(post.getId());

        assertThat(savedPost).contains(post);
        assertThat(foundPost).contains(post);
    }

    @Test
    void whenSaveSameUserAndCarPostThenGetOptionalEmpty() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);
        Post sameUserAndCarPost = new Post();
        sameUserAndCarPost.setUser(user);
        sameUserAndCarPost.setCar(firstCar);
        sameUserAndCarPost.setCreated(LocalDateTime.now().plusDays(1));
        sameUserAndCarPost.setDescription("other description");
        sameUserAndCarPost.setSold(true);
        sameUserAndCarPost.setPrice(2L);

        assertThat(repository.save(post)).contains(post);
        assertThat(repository.save(sameUserAndCarPost)).isEmpty();
    }

    @Test
    void whenFindByWrongIdThenGetOptionalEmpty() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);

        assertThat(repository.save(post)).contains(post);
        assertThat(repository.findById(post.getId() + 1)).isEmpty();
    }

    @Test
    void whenFindAllNotSoldThenGetListOfPosts() {
        Post notSold = new Post();
        notSold.setUser(user);
        notSold.setCar(firstCar);
        notSold.setCreated(LocalDateTime.now());
        notSold.setDescription("description");
        notSold.setSold(false);
        notSold.setPrice(1L);
        Post sold = new Post();
        sold.setUser(user);
        sold.setCar(secondCar);
        sold.setCreated(LocalDateTime.now());
        sold.setDescription("description");
        sold.setSold(true);
        sold.setPrice(1L);

        repository.save(notSold);
        repository.save(sold);

        assertThat(repository.findAllNotSold()).containsExactly(notSold);
        assertThat(repository.findAllNotSold()).doesNotContainSequence(sold);
    }

    @Test
    void whenFindAllByUserIdThenGerListOfPosts() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);

        repository.save(post);

        assertThat(repository.findAllByUserId(user.getId())).containsExactly(post);
    }

    @Test
    void whenUpdatePostThenGetTrueAndPostUpdated() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(post.getPrice());
        post.setPriceHistories(Set.of(priceHistory));

        repository.save(post);
        post.setSold(true);
        post.setDescription("updated");
        post.setPrice(2L);

        assertThat(repository.update(post)).isTrue();
        assertThat(repository.findById(post.getId()).get())
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(post);
    }

    @Test
    void whenUpdatePostWithWrongIdThenGetFalse() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);

        repository.save(post);
        post.setId(post.getId() + 1);
        post.setSold(true);
        post.setDescription("updated");
        post.setPrice(2L);

        assertThat(repository.update(post)).isFalse();
    }

    @Test
    void whenDeleteAllByUserAndFindByUserIdThenGetEmptyList() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);

        repository.save(post);
        repository.deleteAllByUser(user);

        assertThat(repository.findAllByUserId(user.getId())).isEmpty();
    }

    @Test
    void whenFindAllByCriteriaThenGetListOfPosts() {
        Post post = new Post();
        post.setUser(user);
        post.setCar(firstCar);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);

        repository.save(post);
        List<Post> rightCriteriaSearchResult = repository.findAllByCriteria(firstCar, false, 1, 0, 10);
        List<Post> wrongCriteriaSearchResult = repository.findAllByCriteria(secondCar, false, 1, 0, 10);

        assertThat(rightCriteriaSearchResult).containsExactly(post);
        assertThat(wrongCriteriaSearchResult).isEmpty();
    }

}