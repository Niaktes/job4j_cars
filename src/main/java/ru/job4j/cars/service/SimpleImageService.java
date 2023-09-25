package ru.job4j.cars.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;
import ru.job4j.cars.repository.ImageRepository;
import ru.job4j.cars.utilities.ImageUtil;

@Service
@AllArgsConstructor
public class SimpleImageService implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageUtil imageUtil;

    @Override
    public ImageDto getDefaultImageDto() {
        Image image = imageRepository.getDefaultImage();
        return imageUtil.getImageDto(image);
    }

    @Override
    public Set<Image> getImagesByPostId(int id) {
        return new HashSet<>(imageRepository.getImagesByPostId(id));
    }

    @Override
    public Set<Image> saveImages(Collection<ImageDto> imagesDto) {
        return imagesDto.stream()
                .map(imageUtil::saveImage)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ImageDto> getImagesDto(Collection<Image> images) {
        return images.stream()
                .map(imageUtil::getImageDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteImages(Set<Image> images) {
        images.forEach(imageUtil::deleteImage);
    }

}