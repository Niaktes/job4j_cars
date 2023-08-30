package cars.repository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.*;

class UserRepositoryTest {

    private static StandardServiceRegistry registry;
    private static UserRepository repository;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new UserRepository(new CrudRepository(sf));
    }

    @AfterEach
    void clearTable() {
        repository.findAllOrderedById().forEach(el -> repository.delete(el.getId()));
    }

    @AfterAll
    static void cleanAndClose() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenNothingAddedThenFindAllEmptyCollection() {
        assertEquals(List.of(), repository.findAllOrderedById());
    }

    @Test
    void whenCreateAndFindByIdThenGetSame() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");

        repository.create(user);

        assertEquals(Optional.of(user), repository.findById(user.getId()));
    }

    @Test
    void whenAddSameLoginUserThenGetOptionalEmpty() {
        User user = new User();
        User sameLoginUser = new User();
        user.setLogin("login");
        user.setPassword("password");
        sameLoginUser.setLogin(user.getLogin());
        sameLoginUser.setPassword("pass");

        repository.create(user);

        assertEquals(Optional.empty(), repository.create(sameLoginUser));
    }

    @Test
    void whenAddNoLoginOrPasswordUserThenGetOptionalEmpty() {
        User noLoginUser = new User();
        noLoginUser.setPassword("password");
        User noPasswordUser = new User();
        noPasswordUser.setLogin("login");

        assertEquals(Optional.empty(), repository.create(noLoginUser));
        assertEquals(Optional.empty(), repository.create(noPasswordUser));
    }

    @Test
    void whenAddAndUpdateThenGetUpdatedEntity() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        User updatedUser = new User();
        updatedUser.setLogin("updated");
        updatedUser.setPassword("updated");

        repository.create(user);
        int id = user.getId();
        updatedUser.setId(id);
        repository.update(updatedUser);

        assertEquals(Optional.of(updatedUser), repository.findById(id));
    }

    @Test
    void whenAddAndDeleteThenGetListWithoutDeleted() {
        User first = new User();
        first.setLogin("first");
        first.setPassword("first");
        User second = new User();
        second.setLogin("second");
        second.setPassword("second");
        User third = new User();
        third.setLogin("third");
        third.setPassword("third");

        repository.create(first);
        repository.create(second);
        repository.create(third);
        repository.delete(second.getId());

        assertEquals(List.of(first, third), repository.findAllOrderedById());
        assertEquals(Optional.empty(), repository.findById(second.getId()));
    }

    @Test
    void whenFindAllOrderedWhenGetOrderedCollection() {
        User first = new User();
        first.setLogin("first");
        first.setPassword("first");
        User second = new User();
        second.setLogin("second");
        second.setPassword("second");
        User third = new User();
        third.setLogin("third");
        third.setPassword("third");

        repository.create(first);
        repository.create(second);
        repository.create(third);

        assertEquals(List.of(first, second, third), repository.findAllOrderedById());
        assertNotEquals(List.of(second, first, third), repository.findAllOrderedById());
    }

    @Test
    void whenFindByLoginThenGetUser() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");

        repository.create(user);

        assertEquals(Optional.of(user), repository.findByLogin("login"));
        assertEquals(Optional.empty(), repository.findByLogin("log"));
    }

    @Test
    void whenFindByLikeLoginThenGetUser() {
        User first = new User();
        first.setLogin("first");
        first.setPassword("first");
        User second = new User();
        second.setLogin("second");
        second.setPassword("second");

        repository.create(first);
        repository.create(second);

        assertEquals(List.of(first, second), repository.findByLikeLogin("s"));
        assertEquals(List.of(), repository.findByLikeLogin("login"));
        assertEquals(List.of(first), repository.findByLikeLogin("irs"));
    }

}