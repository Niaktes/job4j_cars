package ru.job4j.cars.repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

@Repository
@AllArgsConstructor
public class HibernatePriceHistoryRepository implements PriceHistoryRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД всю историю цен по id объявления.
     * @param id ID объявления.
     * @return множество историй цен.
     */
    @Override
    public Set<PriceHistory> getPriceHistoriesByPostId(int id) {
        return new HashSet<>(crudRepository.query("FROM PriceHistory WHERE post_id = :pId",
                PriceHistory.class,
                Map.of("pId", id)));
    }

}