package cars.util;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;

@AllArgsConstructor
public class HibernateTestUtility {

    private final CrudRepository crudRepository;

    public <T> void save(T element) {
        crudRepository.run(session -> session.saveOrUpdate(element));
    }

    public <T> void delete(T element) {
        crudRepository.run(session -> session.delete(element));
    }

    public <T> void deleteAll(Collection<T> elements) {
        elements.forEach(el -> crudRepository.run(session -> session.delete(el)));
    }

    public User addUserToDatabase() {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setName("name");
        user.setPhone("phone");
        save(user);
        return user;
    }

    public Post addPostToDatabase(User user, Car car) {
        Post post = new Post();
        post.setUser(user);
        post.setCar(car);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);
        save(post);
        return post;
    }

    public Car addCarToDatabase() {
        Brand brand = new Brand();
        brand.setName("brand");
        CarModel model = new CarModel();
        model.setName("model");
        Body body = new Body();
        body.setName("body");
        FuelType fuelType = new FuelType();
        fuelType.setName("fuelType");
        EngineSize engineSize = new EngineSize();
        engineSize.setSize(1.1f);
        Engine engine = new Engine();
        engine.setFuelType(fuelType);
        engine.setEngineSize(engineSize);
        Transmission transmission = new Transmission();
        transmission.setName("transmission");
        Color color = new Color();
        color.setName("color");
        Category category = new Category();
        category.setName("category");

        save(brand);
        model.setBrandId(brand.getId());
        save(model);
        save(body);
        save(fuelType);
        save(engineSize);
        save(engine);
        save(transmission);
        save(color);
        save(category);

        Car car = new Car();
        car.setBrand(brand);
        car.setCarModel(model);
        car.setBody(body);
        car.setEngine(engine);
        car.setTransmission(transmission);
        car.setColor(color);
        car.setCategory(category);
        car.setMileage(1);
        car.setYear(2001);
        car.setVin("Vin");

        save(car);
        return car;
    }

    public void clearDatabase() {
        crudRepository.run(session -> session.createQuery("DELETE PriceHistory").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Post").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Image").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE User").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Car").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Body").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE CarModel").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Brand").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Category").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Color").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Engine").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE EngineSize").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE FuelType").executeUpdate());
        crudRepository.run(session -> session.createQuery("DELETE Transmission").executeUpdate());
    }

}