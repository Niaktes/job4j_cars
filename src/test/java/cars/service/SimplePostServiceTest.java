package cars.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SimplePostServiceTest {

    @MockBean
    private PostRepository postRepository;
    @MockBean
    private CarModelService carModelService;
    @MockBean
    private CarService carService;
    @MockBean
    private EngineService engineService;
    @MockBean
    private ImageService imageService;
    @MockBean
    private PriceHistoryService priceHistoryService;

    private SimplePostService service;

    @BeforeEach
    public void init() {
        service = new SimplePostService(postRepository, carModelService, carService, engineService,
                imageService, priceHistoryService);
    }

    @Test
    void whenSavePostWithImageThenGetPostOptional() {
        Post post = getPost();
        ImageDto imageDto = new ImageDto("name", new byte[1]);
        Brand expectedBrand = new Brand();
        expectedBrand.setId(post.getCar().getCarModel().getBrandId());
        Engine engine = new Engine();
        engine.setId(1);
        PriceHistory expectedPriceHistory = new PriceHistory();
        expectedPriceHistory.setPrice(post.getPrice());
        Image image = new Image();
        image.setId(1);

        when(postRepository.save(any(Post.class))).thenReturn(Optional.of(post));
        when(carModelService.getById(any(Integer.class))).thenReturn(post.getCar().getCarModel());
        when(engineService.findByFuelTypeAndSize(any(), any())).thenReturn(Optional.of(engine));
        when(imageService.saveImage(any(ImageDto.class))).thenReturn(image);
        Optional<Post> actual = service.save(post, imageDto);

        assertThat(actual).isPresent();
        assertThat(actual.get().getCar().getBrand()).isEqualTo(expectedBrand);
        assertThat(actual.get().getCar().getEngine()).isEqualTo(engine);
        assertThat(actual.get().getPriceHistories()).contains(expectedPriceHistory);
        assertThat(actual.get().getImage()).isEqualTo(image);
    }

    @Test
    void whenUpdateAndPriceChangedThenGetTrueAndNewPriceHistory() {
        Post post = getPost();
        ImageDto imageDto = new ImageDto("name", new byte[0]);
        Brand expectedBrand = new Brand();
        expectedBrand.setId(post.getCar().getCarModel().getBrandId());
        Engine engine = new Engine();
        engine.setId(1);
        PriceHistory expectedPriceHistory = new PriceHistory();
        expectedPriceHistory.setPrice(1L);
        Set<PriceHistory> priceHistories = new HashSet<>();
        priceHistories.add(new PriceHistory());

        when(carModelService.getById(any(Integer.class))).thenReturn(post.getCar().getCarModel());
        when(engineService.findByFuelTypeAndSize(any(), any())).thenReturn(Optional.of(engine));
        when(priceHistoryService.getPriceHistoriesByPostId(any(Integer.class)))
                .thenReturn(priceHistories);
        when(postRepository.update(any(Post.class))).thenReturn(true);

        assertThat(service.update(post, imageDto)).isTrue();
        assertThat(post.getCar().getBrand()).isEqualTo(expectedBrand);
        assertThat(post.getCar().getEngine()).isEqualTo(engine);
        assertThat(post.getPriceHistories()).contains(expectedPriceHistory);
    }

    @Test
    void whenUpdateAndPriceNotChangedThenGetTrue() {
        Post post = getPost();
        ImageDto imageDto = new ImageDto("name", new byte[0]);
        Engine engine = new Engine();
        PriceHistory expectedPriceHistory = new PriceHistory();
        expectedPriceHistory.setPrice(post.getPrice());

        when(carModelService.getById(any(Integer.class))).thenReturn(post.getCar().getCarModel());
        when(engineService.findByFuelTypeAndSize(any(), any())).thenReturn(Optional.of(engine));
        when(priceHistoryService.getPriceHistoriesByPostId(any(Integer.class)))
                .thenReturn(Set.of(expectedPriceHistory));
        when(postRepository.update(any(Post.class))).thenReturn(true);

        assertThat(service.update(post, imageDto)).isTrue();
        assertThat(post.getPriceHistories()).containsExactly(expectedPriceHistory);
    }

    @Test
    void whenUpdateWithNewImageThenGetTrueAndNewPostImage() {
        Post post = getPost();
        ImageDto imageDto = new ImageDto("name", new byte[1]);
        Engine engine = new Engine();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(post.getPrice());
        Image image = new Image();
        image.setId(1);

        when(carModelService.getById(any(Integer.class))).thenReturn(post.getCar().getCarModel());
        when(engineService.findByFuelTypeAndSize(any(), any())).thenReturn(Optional.of(engine));
        when(priceHistoryService.getPriceHistoriesByPostId(any(Integer.class)))
                .thenReturn(Set.of(priceHistory));
        when(imageService.saveImage(any(ImageDto.class))).thenReturn(image);
        when(postRepository.update(any(Post.class))).thenReturn(true);

        assertThat(service.update(post, imageDto)).isTrue();
        assertThat(post.getImage()).isEqualTo(image);
        verify(imageService, never()).deleteImage(image);
    }

    private Post getPost() {
        Post post = new Post();
        post.setPrice(1L);
        post.setDescription("description");
        post.setSold(false);

        Car car = new Car();
        CarModel model = new CarModel();
        Engine engine = new Engine();
        engine.setEngineSize(new EngineSize());
        engine.setFuelType(new FuelType());
        car.setCarModel(model);
        car.setEngine(engine);

        post.setCar(car);
        return post;
    }

}