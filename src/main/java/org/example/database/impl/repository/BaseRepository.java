package org.example.database.impl.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class BaseRepository<T> {
    protected Class<T> tClass;
    private SessionFactory sessionFactory;

    @Inject
    @SuppressWarnings("unchecked")
    void init(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        tClass = (Class<T>) (
                (ParameterizedType) this.getClass().getGenericSuperclass()
        ).getActualTypeArguments()[0];
    }

    public Optional<T> findById(UUID id) {
        return tryWithNewSession(
                session -> session.byId(tClass).loadOptional(id)
        );
    }

    public T save(T entity) {
        return tryWithNewSession(
                session -> {
                    session.saveOrUpdate(entity);
                    return entity;
                }
        );
    }

    protected <R> R tryWithNewSession(Function<Session, R> function) {
        //TODO сессия для каждой операции - не оптимально, нужно сессия на запрос
        // не разобрался как сделать правильно.
        // вариант 1: через @PersistentContext - в поле будет инжектиться прокси EM, прокси
        // потокобезопасно и будет брать на себя закрытие сессии перед ответом клиенту
        // проблема: класс должен быть инициализирован контейнером, не понятно как
        // получить экземпляр для внедрения через Guice
        // вариант 2, изоленто-палочный: использовать @RequestScoped, чтобы через Guice
        // создавать сессии под запрос, закрывать через перехватичик ответов, но еще нужно как-то
        // ловить исключения
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
