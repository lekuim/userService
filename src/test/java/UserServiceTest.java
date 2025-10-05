import org.example.dao.UserDao;
import org.example.service.UserService;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
    }

    @Test
    void testCreateUser() {
        String input = "Дима\ndmitry@example.com\n17\n";
        userService = new UserService(userDao, new Scanner(new ByteArrayInputStream(input.getBytes())));

        userService.createUser();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(captor.capture());
        User created = captor.getValue();

        assertEquals("Дима", created.getName());
        assertEquals("dmitry@example.com", created.getEmail());
        assertEquals(17, created.getAge());
    }

    @Test
    void testViewUserByIdFound() {
        String input = "1\n";
        userService = new UserService(userDao, new Scanner(new ByteArrayInputStream(input.getBytes())));
        User user = new User();
        user.setId(1L);
        user.setName("Артем");
        user.setEmail("artem@example.com");
        user.setAge(30);

        when(userDao.readById(1L)).thenReturn(user);

        userService.viewUserById();

        verify(userDao).readById(1L);
    }

    @Test
    void testViewUserByIdNotFound() {
        String input = "2\n";
        userService = new UserService(userDao, new Scanner(new ByteArrayInputStream(input.getBytes())));

        when(userDao.readById(2L)).thenReturn(null);

        userService.viewUserById();

        verify(userDao).readById(2L);
    }

    @Test
    void testDeleteUser() {
        String input = "5\n";
        userService = new UserService(userDao, new Scanner(new ByteArrayInputStream(input.getBytes())));

        userService.deleteUser();

        verify(userDao).delete(5L);
    }

    @Test
    void testUpdateUser() {
        String input = "1\nЛидия\nstevia@example.com\n38\n";
        userService = new UserService(userDao, new Scanner(new ByteArrayInputStream(input.getBytes())));

        User existing = new User();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setEmail("old@example.com");
        existing.setAge(20);

        when(userDao.readById(1L)).thenReturn(existing);

        userService.updateUser();

        verify(userDao).update(existing);
        assertEquals("Лидия", existing.getName());
        assertEquals("stevia@example.com", existing.getEmail());
        assertEquals(38, existing.getAge());
    }

    @Test
    void testViewAllUsers() {
        userService = new UserService(userDao, new Scanner(""));

        User u1 = new User();
        u1.setId(1L);
        u1.setName("A");

        User u2 = new User();
        u2.setId(2L);
        u2.setName("B");

        when(userDao.readAll()).thenReturn(List.of(u1, u2));

        userService.viewAllUsers();

        verify(userDao).readAll();
    }
}
