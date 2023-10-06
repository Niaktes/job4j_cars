package ru.job4j.cars.controller;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.service.ImageService;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable int id) {
        Optional<ImageDto> imageDtoOptional = imageService.getImageDtoById(id);
        if (imageDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageDtoOptional.get().getContent());
    }

    @GetMapping("/defaultImage")
    public ResponseEntity<?> getDefaultImage() {
        return ResponseEntity.ok(imageService.getDefaultImageDto().getContent());
    }

}