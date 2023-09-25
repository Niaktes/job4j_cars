package ru.job4j.cars.service;

import java.util.Collection;
import java.util.Set;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;

public interface ImageService {

    ImageDto getDefaultImageDto();

    Set<Image> saveImages(Collection<ImageDto> imagesDto);

    Set<Image> getImagesByPostId(int postId);

    Set<ImageDto> getImagesDto(Collection<Image> images);

    void deleteImages(Set<Image> images);

}