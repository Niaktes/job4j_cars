package cars.util;

import java.time.LocalDateTime;
import ru.job4j.cars.model.*;

public class ControllerTestUtil {

    public User makeUser() {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setName("name");
        user.setPhone("phone");
        return user;
    }

    public Car makeCar() {
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

        return car;
    }

    public Post makePost(User user, Car car) {
        Post post = new Post();
        post.setUser(user);
        post.setCar(car);
        post.setCreated(LocalDateTime.now());
        post.setDescription("description");
        post.setSold(false);
        post.setPrice(1L);
        return post;
    }

}