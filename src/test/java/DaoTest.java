import org.example.config.HibernateUtil;
import org.example.entity.User;
import org.example.dao.UserDaoReal;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DaoTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private UserDaoReal userDao;

    @BeforeAll
    void setUp() {
        postgres.start();

        // Настраиваем Hibernate для Testcontainers
        Properties props = new Properties();
        props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        props.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        props.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        props.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "true");


        HibernateUtil.overrideConfiguration(props);

        userDao = new UserDaoReal();
    }

    @AfterAll
    void tearDown() {
        postgres.stop();
    }

    @Test
    void testCreateAndReadById() {
        User user = new User();
        user.setName("дементий");
        user.setEmail("tashipig@example.com");

        userDao.create(user);

        List<User> all = userDao.readAll();
        assertEquals(1, all.size());

        User fetched = all.get(0);
        assertEquals("дементий", fetched.getName());
        assertEquals("tashipig@example.com", fetched.getEmail());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("дементий");
        user.setEmail("tashipig@example.com");
        userDao.create(user);

        User existing = userDao.readAll().get(0);
        existing.setName("дементий");
        userDao.update(existing);

        User updated = userDao.readById(existing.getId());
        assertEquals("дементий", updated.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("дементий");
        user.setEmail("tashipig@example.com");
        userDao.create(user);

        User existing = userDao.readAll().get(0);
        userDao.delete(existing.getId());

        List<User> afterDelete = userDao.readAll();
        assertTrue(afterDelete.isEmpty());
    }
}
