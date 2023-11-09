package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.cars.service.ImageService;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id) {
        return ResponseEntity.ok(imageService.getImageDtoById(id).getContent());
    }

    @GetMapping("/defaultImage")
    public ResponseEntity<byte[]> getDefaultImage() {
        return ResponseEntity.ok(imageService.getDefaultImageDto().getContent());
    }

}