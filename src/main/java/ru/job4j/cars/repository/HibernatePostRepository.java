package ru.job4j.cars.repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import javax.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.*;

@Repository
@AllArgsConstructor
public class HibernatePostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param post пост.
     * @return Optional поста c ID, или пустой Optional при совпадении уникальных полей.
     */
    @Override
    public Optional<Post> save(Post post) {
        Optional<Post> result = Optional.empty();
        if (crudRepository.run(session -> session.save(post))) {
            result = Optional.of(post);
        }
        return result;
    }

    /**
     * Найти пост по ID.
     * @param id ID поста.
     * @return Optional поста.
     */
    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional("FROM Post WHERE id = :pId", Post.class, Map.of("pId", id));
    }

    /**
     * Найти все посты, машины в которых ещё не проданы.
     * @return список постов.
     */
    @Override
    public List<Post> findAllNotSold() {
        return crudRepository.query("FROM Post WHERE sold = :not", Post.class, Map.of("not", false));
    }

    /**
     * Найти все посты конкретного пользователя.
     * @param id ID пользователя
     * @return список постов.
     */
    @Override
    public List<Post> findAllByUserId(int id) {
        return crudRepository.query("FROM Post WHERE auto_user_id = :uId",
                Post.class,
                Map.of("uId", id));
    }

    /**
     * Обновить в базе данных пост.
     * @param post пост.
     * @return true в случае удачного обновления.
     */
    @Override
    public boolean update(Post post) {
        return crudRepository.run(session -> session.update(post));
    }

    /**
     * Удалить пост по id.
     * @param post пост.
     */
    @Override
    public void delete(Post post) {
        crudRepository.run(session -> session.delete(post));
    }

    /**
     * Найти все посты по определенным критериям.
     * @param car критерии автомобиля.
     * @param imagesExist наличие изображений.
     * @param createdDaysBefore количество дней прошедших с создания поста.
     * @param minPrice минимальная цена в посте.
     * @param maxPrice максимальная цена в посте.
     * @return список постов.
     */
    @Override
    public List<Post> findAllByCriteria(Car car, boolean imagesExist, int createdDaysBefore, long minPrice,
                                        long maxPrice) {
        Function<Session, List<Post>> command = session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
            Root<Post> post = criteriaQuery.from(Post.class);
            post.fetch("car", JoinType.LEFT);
            post.fetch("images", JoinType.LEFT);
            post.fetch("priceHistories", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            if (car.getModel() != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("model"), car.getModel()));
            }
            if (car.getBody() != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("body"), car.getBody()));
            }
            if (car.getEngine() != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("engine"), car.getEngine()));
            }
            if (car.getTransmission() != null) {
                predicates.add(criteriaBuilder.equal(
                        post.get("car").get("transmission"),
                        car.getTransmission())
                );
            }
            if (car.getColor() != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("color"), car.getColor()));
            }
            if (car.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("category"), car.getCategory()));
            }
            if (car.getYear() != 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        post.get("car").get("year"),
                        car.getYear())
                );
            }
            if (car.getMileage() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        post.get("car").get("mileage"),
                        car.getMileage())
                );
            }
            if (imagesExist) {
                predicates.add(criteriaBuilder.isNotEmpty(post.get("photos")));
            }
            if (createdDaysBefore > 0) {
                predicates.add(criteriaBuilder.between(
                        post.get("created"),
                        LocalDateTime.now().minusDays(createdDaysBefore),
                        LocalDateTime.now())
                );
            }
            if (minPrice != 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(post.get("price"), minPrice));
            }
            if (maxPrice != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(post.get("price"), maxPrice));
            }
            predicates.add(criteriaBuilder.isFalse(post.get("sold")));
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            return session.createQuery(criteriaQuery).getResultList();
        };
        return crudRepository.tx(command);
    }

}