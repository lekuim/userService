package org.example;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoReal implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoReal.class);

    @Override
    public void create(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(user);
                transaction.commit();
                log.info("User создан: {}\n", user.getName());
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Ошибка при создании пользователя: {}\n", user.getName(), e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Ошибка соединения с базой при создании пользователя\n", e);
        }
    }

    @Override
    public User readById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                log.info("Пользователь найден: {} (ID={})\n", user.getName(), id);
            } else {
                log.warn("Пользователь с ID={} не найден\n", id);
            }
            return user;
        } catch (Exception e) {
            log.error("Ошибка при чтении пользователя по ID={}\n", id, e);
            return null;
        }
    }

    @Override
    public List<User> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            log.info("Найдено {} пользователей\n", users.size());
            return users;
        } catch (Exception e) {
            log.error("Ошибка при чтении всех пользователей\n", e);
            return List.of();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
                log.info("Пользователь обновлен: {} (ID={})\n", user.getName(), user.getId());
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Ошибка при обновлении пользователя: {} (ID={})\n", user.getName(), user.getId(), e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Ошибка соединения с базой при обновлении пользователя\n", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user != null) {
                    session.remove(user);
                    log.info("Пользователь удален: {} (ID={})\n", user.getName(), id);
                } else {
                    log.warn("Попытка удалить несуществующего пользователя с ID={}\n", id);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Ошибка при удалении пользователя с ID={}\n", id, e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Ошибка соединения с базой при удалении пользователя с ID={}\n", id, e);
        }
    }
}
