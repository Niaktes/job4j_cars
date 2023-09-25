package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
    public Optional<Post> save(Post post, Set<ImageDto> imagesDto) {
        Set<Image> newImages = imageService.saveImages(imagesDto);
        post.setImages(newImages);
        Optional<Post> postOptional = postRepository.save(post);
        if (postOptional.isEmpty()) {
            imageService.deleteImages(newImages);
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
    public boolean update(Post post, Set<ImageDto> imagesDto) {
        Set<ImageDto> existedImagesDto = imagesDto.stream()
                .filter(dto -> dto.getContent().length != 0)
                .collect(Collectors.toSet());
        Set<Image> oldImages = imageService.getImagesByPostId(post.getId());
        boolean updated;
        if (existedImagesDto.isEmpty()) {
            post.setImages(oldImages);
            updated = postRepository.update(post);
        } else {
            Set<Image> newImages = imageService.saveImages(existedImagesDto);
            post.setImages(newImages);
            updated = postRepository.update(post);
            if (updated) {
                imageService.deleteImages(oldImages);
            } else {
                imageService.deleteImages(newImages);
            }
        }
        return updated;
    }

    @Override
    public void delete(Post post) {
        Set<Image> postImages = imageService.getImagesByPostId(post.getId());
        imageService.deleteImages(postImages);
        postRepository.delete(post);
    }

    @Override
    public List<Post> findAllByCriteria(Car car, boolean imagesExist, int createdDaysBefore,
                                        long minPrice, long maxPrice) {
        return postRepository.findAllByCriteria(car, imagesExist, createdDaysBefore, minPrice, maxPrice);
    }

}