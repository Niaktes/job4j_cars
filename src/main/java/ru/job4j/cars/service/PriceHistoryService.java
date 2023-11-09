package ru.job4j.cars.service;

import java.util.Set;
import ru.job4j.cars.model.PriceHistory;

public interface PriceHistoryService {

    Set<PriceHistory> getPriceHistoriesByPostId(int id);

}