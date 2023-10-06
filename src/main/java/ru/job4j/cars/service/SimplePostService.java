package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Image;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.PostRepository;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;

    @Override
    public Optional<Post> save(Post post, ImageDto imageDto) {
        Image newImage = imageService.saveImage(imageDto);
        post.setImage(newImage);
        Optional<Post> postOptional = postRepository.save(post);
        if (postOptional.isEmpty()) {
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
        boolean isNewImageExists = imageDto.getContent().length != 0;
        if (!isNewImageExists) {
            return postRepository.update(post);
        }
        Optional<Image> oldImage = post.getImage() != null
                ? imageService.getImageById(post.getImage().getId()) : Optional.empty();
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
    public void delete(Post post) {
        Optional<Image> postImage = post.getImage() != null
                ? imageService.getImageById(post.getImage().getId()) : Optional.empty();
        postImage.ifPresent(imageService::deleteImage);
        postRepository.delete(post);
    }

    @Override
    public List<Post> findAllByCriteria(Car car, boolean imageExist, int createdDaysBefore,
                                        long minPrice, long maxPrice) {
        return postRepository.findAllByCriteria(car, imageExist, createdDaysBefore, minPrice, maxPrice);
    }

}