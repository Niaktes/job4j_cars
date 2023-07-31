package ru.job4j.cars.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@AllArgsConstructor
public class CrudRepository {

    private final SessionFactory sf;

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return  sq.list();
        };
        return tx(command);
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

}