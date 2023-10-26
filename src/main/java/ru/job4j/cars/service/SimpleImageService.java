package ru.job4j.cars.service;

import java.util.Optional;
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
        return imageUtil.getImageDto(imageRepository.getDefaultImage());
    }

    @Override
    public ImageDto getImageDtoById(int id) {
        return imageUtil.getImageDto(imageRepository.findById(id).get());
    }

    @Override
    public Optional<Image> getImageById(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image saveImage(ImageDto imageDto) {
        return imageUtil.saveImage(imageDto);
    }

    @Override
    public void deleteImage(Image image) {
        imageUtil.deleteImage(image);
    }

}