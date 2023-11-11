package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

    private String name;
    private byte[] content;

}