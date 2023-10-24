package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.Car;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchDto {

    private Car car;
    private long highestPrice;
    private long lowestPrice;
    private int postCreatedBeforeDays;
    private boolean imageExists;

}