package ru.job4j.cars.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;

@Component
public class ImageUtil {

    private final String imageDirectory;

    public ImageUtil(@Value("${image.directory}") String imageDirectory) {
        this.imageDirectory = imageDirectory;
        createImageDirectory(imageDirectory);
    }

    public ImageDto getImageDto(Image image) {
        return new ImageDto(image.getName(), readFileAsBytes(image.getPath()));
    }

    public Image saveImage(ImageDto imageDto) {
        String path = imageDirectory + File.separator + UUID.randomUUID() + imageDto.getName();
        writeFileBytes(path, imageDto.getContent());
        Image image = new Image();
        image.setName(imageDto.getName());
        image.setPath(path);
        return image;
    }

    public void deleteImage(Image image) {
        try {
            Files.deleteIfExists(Path.of(image.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createImageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}