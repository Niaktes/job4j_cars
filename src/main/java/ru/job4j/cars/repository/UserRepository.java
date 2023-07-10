package ru.job4j.cars.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sf;

    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return user;
    }

    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(int userId) {
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            session.createQuery("DELETE User AS u WHERE u.id = :uId")
                    .setParameter("uId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<User> findAllOrderedById() {
        Session session = sf.openSession();
        List<User> result = List.of();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User ORDER BY id", User.class);
            result = query.list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User AS u WHERE u.id = :uId", User.class);
            query.setParameter("uId", userId);
            result = query.uniqueResultOptional();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        List<User> result = List.of();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User AS u WHERE u.login LIKE :uKey", User.class);
            query.setParameter("uKey", "%" + key + "%");
            result = query.list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User AS u WHERE u.login = :uLogin", User.class);
            query.setParameter("uLogin", login);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

}