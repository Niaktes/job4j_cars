package cars.testUtil;

import java.util.Collection;
import lombok.AllArgsConstructor;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;

@AllArgsConstructor
public class HibernateTestUtility {

    private final CrudRepository crudRepository;

    public <T> void save(T element) {
        crudRepository.run(session -> session.save(element));
    }

    public <T> void delete(T element) {
        crudRepository.run(session -> session.delete(element));
    }

    public <T> void deleteAll(Collection<T> elements) {
        elements.forEach(el -> crudRepository.run(session -> session.delete(el)));
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

    public void deleteCarWithComponentsFromDatabase(Car car) {
        delete(car);
        delete(car.getCategory());
        delete(car.getColor());
        delete(car.getTransmission());
        delete(car.getEngine());
        delete(car.getEngine().getEngineSize());
        delete(car.getEngine().getFuelType());
        delete(car.getBody());
        delete(car.getCarModel());
        delete(car.getBrand());
    }

}