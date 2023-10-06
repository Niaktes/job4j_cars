package ru.job4j.cars.service;

import java.util.Optional;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;

public interface ImageService {

    ImageDto getDefaultImageDto();

    Optional<ImageDto> getImageDtoById(int id);

    Optional<Image> getImageById(int id);

    Image saveImage(ImageDto imageDto);

    void deleteImage(Image image);

}