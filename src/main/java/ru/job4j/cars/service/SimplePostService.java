package ru.job4j.cars.service;

import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.PostRepository;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;
    private final CarModelService carModelService;
    private final CarService carService;
    private final BrandService brandService;
    private final EngineService engineService;
    private final ImageService imageService;
    private final PriceHistoryService priceHistoryService;

    @Override
    public Optional<Post> save(Post post, ImageDto imageDto) {
        setBrand(post.getCar());
        setEngine(post.getCar());
        Image newImage = null;
        if (imageDto.getContent().length != 0) {
            newImage = imageService.saveImage(imageDto);
            post.setImage(newImage);
        }
        addPriceHistory(post);
        Optional<Post> postOptional = postRepository.save(post);
        if (postOptional.isEmpty() && newImage != null) {
            imageService.deleteImage(newImage);
        }
        return postOptional;
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAllNotSold() {
        return postRepository.findAllNotSold();
    }

    @Override
    public List<Post> findAllByUserId(int id) {
        return postRepository.findAllByUserId(id);
    }

    @Override
    public boolean update(Post post, ImageDto imageDto) {
        setBrand(post.getCar());
        setEngine(post.getCar());
        post.setPriceHistories(priceHistoryService.getPriceHistoriesByPostId(post.getId()));
        PriceHistory lastPrice = Collections.max(post.getPriceHistories(),
                Comparator.comparing(PriceHistory::getDate));
        if (post.getPrice() != lastPrice.getPrice()) {
            addPriceHistory(post);
        }

        boolean isNewImageExists = imageDto.getContent().length != 0;
        Optional<Image> oldImage = post.getImage() != null
                ? imageService.getImageById(post.getImage().getId()) : Optional.empty();
        if (!isNewImageExists) {
            oldImage.ifPresent(post::setImage);
            return postRepository.update(post);
        }
        Image newImage = imageService.saveImage(imageDto);
        post.setImage(newImage);
        boolean isUpdated = postRepository.update(post);
        if (isUpdated) {
            oldImage.ifPresent(imageService::deleteImage);
        } else {
            imageService.deleteImage(newImage);
        }
        return isUpdated;
    }

    @Override
    public void deleteAllByUser(User user) {
        List<Post> posts = postRepository.findAllByUserId(user.getId());
        posts.forEach(this::deletePostsImage);
        posts.forEach(p -> carService.delete(p.getCar()));
        postRepository.deleteAllByUser(user);
    }

    @Override
    public List<Post> findAllByCriteria(PostSearchDto searchDto) {
        return postRepository.findAllByCriteria(
                searchDto.getCar(),
                searchDto.isImageExists(),
                searchDto.getPostCreatedBeforeDays(),
                searchDto.getLowestPrice(),
                searchDto.getHighestPrice());
    }

    private Car setBrand(Car car) {
        CarModel carModel = carModelService.getById(car.getCarModel().getId());
        Brand brand = brandService.getById(carModel.getBrandId());
        car.setBrand(brand);
        return car;
    }

    private Car setEngine(Car car) {
        Optional<Engine> engineOptional = engineService.findByFuelTypeAndSize(
                car.getEngine().getFuelType(), car.getEngine().getEngineSize());
        if (engineOptional.isEmpty()) {
            Engine engine = engineService.save(car.getEngine());
            car.setEngine(engine);
        } else {
            car.setEngine(engineOptional.get());
        }
        return car;
    }

    private void deletePostsImage(Post post) {
        Optional<Image> postImage = post.getImage() != null
                ? imageService.getImageById(post.getImage().getId()) : Optional.empty();
        postImage.ifPresent(imageService::deleteImage);
    }

    private Post addPriceHistory(Post post) {
        PriceHistory newPriceHistory = new PriceHistory();
        newPriceHistory.setPrice(post.getPrice());
        post.getPriceHistories().add(newPriceHistory);
        return post;
    }

}