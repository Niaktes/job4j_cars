package cars.repository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.HibernateUserRepository;

class HibernateUserRepositoryTest {

    private static StandardServiceRegistry registry;
    private static HibernateUserRepository repository;
    private static User user;

    @BeforeAll
    static void initialization() {
        registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new HibernateUserRepository(new CrudRepository(sf));
        user = new User();
        user.setName("name");
        user.setPhone("88005553535");
        user.setEmail("email");
        user.setPassword("password");
    }

    @AfterEach
    void clearTable() {
        repository.deleteByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @AfterAll
    static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenSaveAndFindUserThenGetUserOptional() {
        repository.save(user);
        assertThat(repository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .contains(user);
    }

    @Test
    void whenTryToSaveNotUniqueEmailOrPhoneThenGetOptionalEmpty() {
        User samePhoneUser = new User();
        samePhoneUser.setName("samePhoneName");
        samePhoneUser.setPhone(user.getPhone());
        samePhoneUser.setEmail("samePhoneEmail");
        samePhoneUser.setPassword(user.getPassword());
        User sameEmailUser = new User();
        sameEmailUser.setName("sameEmailName");
        sameEmailUser.setPhone("sameEmailPhone");
        sameEmailUser.setEmail(user.getEmail());
        sameEmailUser.setPassword(user.getPassword());

        repository.save(user);
        Optional<User> savedSamePhoneUser = repository.save(samePhoneUser);
        Optional<User> savedSameEmailUser = repository.save(sameEmailUser);

        assertThat(savedSamePhoneUser).isEmpty();
        assertThat(savedSameEmailUser).isEmpty();
    }

    @Test
    void whenUpdateAndGetUserThenGetUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setName("updatedName");
        updatedUser.setPhone("updatedPhone");
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());

        repository.save(user);
        updatedUser.setId(user.getId());
        boolean isUpdated = repository.update(updatedUser);

        assertThat(isUpdated).isTrue();
        assertThat(repository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .contains(updatedUser);
    }

    @Test
    void whenTryToUpdateUserWithWrongIdThenGetFalse() {
        User updatedUser = new User();
        updatedUser.setId(0);

        repository.save(user);
        boolean isUpdated = repository.update(updatedUser);

        assertThat(isUpdated).isFalse();
    }

    @Test
    void whenFindByWrongEmailOrPasswordThenGetOptionalEmpty() {
        repository.save(user);
        Optional<User> wrongEmail = repository.findByEmailAndPassword("", user.getPassword());
        Optional<User> wrongPassword = repository.findByEmailAndPassword(user.getEmail(), "");

        assertThat(wrongEmail).isEmpty();
        assertThat(wrongPassword).isEmpty();
    }

    @Test
    void whenDeleteAndFindThenGetOptionalEmpty() {
        repository.save(user);
        repository.deleteByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(repository.findByEmailAndPassword(user.getEmail(), user.getPassword())).isEmpty();
    }

}