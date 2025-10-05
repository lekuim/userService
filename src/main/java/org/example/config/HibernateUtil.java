package org.example.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) { // если ещё не создано
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Ошибка инициализации Hibernate: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static void overrideConfiguration(Properties properties) {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(org.example.entity.User.class);
            configuration.addProperties(properties);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось переопределить Hibernate конфигурацию", e);
        }
    }
}
