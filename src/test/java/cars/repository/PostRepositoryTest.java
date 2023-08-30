package cars.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

class PostRepositoryTest {

    private static StandardServiceRegistry registry;
    private static EngineRepository engineRepository;
    private static ModelRepository modelRepository;
    private static CarRepository carRepository;
    private static UserRepository userRepository;
    private static PostRepository postRepository;
    private static Engine engine;
    private static CarModel model;
    private static Car car;
    private static User user;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        engineRepository = new EngineRepository(crudRepository);
        modelRepository = new ModelRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
        postRepository = new PostRepository(crudRepository);

        engine = new Engine();
        engine.setName("engine");
        model = new CarModel();
        model.setName("model");
        model.setBrandName("modelBrand");
        car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setCarModel(model);
        user = new User();
        user.setLogin("login");
        user.setPassword("password");

        engineRepository.create(engine);
        modelRepository.create(model);
        carRepository.create(car);
        userRepository.create(user);
    }

    @AfterEach
    void clearTable() {
        postRepository.findAllByUserId(user.getId()).forEach(el -> postRepository.delete(el));
    }

    @AfterAll
    static void cleanAndClose() {
        userRepository.delete(user.getId());
        carRepository.delete(car.getId());
        modelRepository.delete(model.getId());
        engineRepository.delete(engine.getId());
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenAddAndFindByIdThenGetOptionalPost() {
        Post post = new Post();
        post.setDescription("description");
        post.setUser(user);
        post.setCar(car);

        postRepository.create(post);

        assertEquals(Optional.of(post), postRepository.findById(post.getId()));
    }

    @Test
    void whenAddAndFindByUserIdThenGetPostCollection() {
        Post post = new Post();
        post.setDescription("description");
        post.setUser(user);
        post.setCar(car);

        postRepository.create(post);

        assertEquals(List.of(post), postRepository.findAllByUserId(user.getId()));
        assertEquals(List.of(), postRepository.findAllByUserId(user.getId() - 1));
    }

    @Test
    void whenAddAndFindInLastDaysThenGetPostCollection() {
        Post first = new Post();
        first.setDescription("first");
        first.setUser(user);
        first.setCar(car);
        Post second = new Post();
        second.setDescription("second");
        second.setUser(user);
        second.setCar(car);
        second.setCreated(second.getCreated().minusDays(2));

        postRepository.create(first);
        postRepository.create(second);

        assertEquals(List.of(first), postRepository.findAllInLastDays(1));
        assertEquals(List.of(first, second), postRepository.findAllInLastDays(3));
    }

    @Test
    void whenAddAndFindWithPhotoThenGetPostCollection() {
        Post withPhoto = new Post();
        withPhoto.setDescription("photo");
        withPhoto.setUser(user);
        withPhoto.setCar(car);
        Photo photo = new Photo();
        photo.setName("name");
        photo.setPath("path");
        withPhoto.setPhotos(Set.of(photo));
        Post noPhoto = new Post();
        noPhoto.setDescription("noPhoto");
        noPhoto.setUser(user);
        noPhoto.setCar(car);

        postRepository.create(withPhoto);
        postRepository.create(noPhoto);

        assertEquals(List.of(withPhoto), postRepository.findAllWithPhoto());
    }

    @Test
    void whenAddAndFindByCarModelThenGetPostCollection() {
        Post post = new Post();
        post.setDescription("description");
        post.setUser(user);
        post.setCar(car);

        postRepository.create(post);

        assertEquals(List.of(post), postRepository.findAllOfModel(model));
    }

    @Test
    void whenAddAndUpdateThenGetUpdated() {
        Post post = new Post();
        post.setDescription("description");
        post.setUser(user);
        post.setCar(car);
        Post updated = new Post();
        updated.setDescription("updated");
        updated.setUser(user);
        updated.setCar(car);

        postRepository.create(post);
        int id = post.getId();
        updated.setId(id);
        postRepository.update(updated);

        assertEquals(Optional.of(updated), postRepository.findById(id));
    }

    @Test
    void whenAddAndDeleteThenGetCollectionWithoutDeleted() {
        Post first = new Post();
        first.setDescription("first");
        first.setUser(user);
        first.setCar(car);
        Post second = new Post();
        second.setDescription("second");
        second.setUser(user);
        second.setCar(car);

        postRepository.create(first);
        postRepository.create(second);
        postRepository.delete(first);

        assertEquals(List.of(second), postRepository.findAllByUserId(user.getId()));
    }

}