package ru.job4j.cars.repository;

import java.util.Set;
import ru.job4j.cars.model.PriceHistory;

public interface PriceHistoryRepository {

    Set<PriceHistory> getPriceHistoriesByPostId(int id);

}