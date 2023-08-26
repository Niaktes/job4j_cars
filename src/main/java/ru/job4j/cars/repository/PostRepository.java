package ru.job4j.cars.repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import javax.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

@AllArgsConstructor
public class PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе данных.
     * @param post пост.
     * @return пост с id.
     */
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    /**
     * Найти пост по ID.
     * @param id ID поста.
     * @return Optional поста.
     */
    public Optional<Post> findById(int id) {
        return crudRepository.optional("FROM Post WHERE id = :pId", Post.class, Map.of("pId", id));
    }

    /**
     * Найти все посты конкретного пользователя.
     * @param id ID пользователя
     * @return список постов.
     */
    public List<Post> findAllByUserId(int id) {
        return crudRepository.query("FROM Post WHERE auto_user_id = :uId",
                Post.class,
                Map.of("uId", id));
    }

    /**
     * Найти все посты за последние несколько дней.
     * @param days количество дней.
     * @return список постов.
     */
    public List<Post> findAllInLastDays(int days) {
        return findAllByCriteria(days, null, false, null, null, null, -1);
    }

    /**
     * Найти все посты где есть хоть одна фотография.
     * @return список постов.
     */
    public List<Post> findAllWithPhoto() {
        return findAllByCriteria(0, null, true, null, null, null, -1);
    }

    /**
     * Найти все посты, где модель автомобиля определенного типа.
     * @param carModel модель автомобиля.
     * @return список постов.
     */
    public List<Post> findAllOfModel(CarModel carModel) {
        return findAllByCriteria(0, null, false, null, carModel, null, -1);
    }

    /**
     * Обновить в базе данных пост.
     * @param post пост.
     * @return true в случае удачного обновления.
     */
    public boolean update(Post post) {
        return crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить пост по id.
     * @param postId ID.
     * @return true в случае удачного удаления.
     */
    public boolean delete(int postId) {
        return crudRepository.run("DELETE FROM Post WHERE id = :pId", Map.of("pId", postId));
    }

    private List<Post> findAllByCriteria(int createdDaysBefore, User user, boolean photosExist,
                                         String carName, CarModel carModel, Engine engine, int ownersNumber) {
        Function<Session, List<Post>> command = session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
            Root<Post> post = criteriaQuery.from(Post.class);
            post.fetch("priceHistories", JoinType.LEFT);
            post.fetch("photos", JoinType.LEFT);
            post.fetch("participates", JoinType.LEFT);
            post.fetch("car", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            if (createdDaysBefore > 0) {
                predicates.add(criteriaBuilder.between(
                        post.get("created"),
                        LocalDateTime.now().minusDays(createdDaysBefore),
                        LocalDateTime.now()));
            }
            if (user != null) {
                predicates.add(criteriaBuilder.equal(post.get("user"), user));
            }
            if (photosExist) {
                predicates.add(criteriaBuilder.isNotEmpty(post.get("photos")));
            }
            if (carName != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("name"), carName));
            }
            if (carModel != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("carModel"), carModel));
            }
            if (engine != null) {
                predicates.add(criteriaBuilder.equal(post.get("car").get("engine"), engine));
            }
            if (ownersNumber == 0) {
                predicates.add(criteriaBuilder.isEmpty(post.get("car").get("history")));
            }
            if (ownersNumber > 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        post.get("car").get("history"),
                        ownersNumber));
            }
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            return session.createQuery(criteriaQuery).getResultList();
        };
        return crudRepository.tx(command);
    }

}