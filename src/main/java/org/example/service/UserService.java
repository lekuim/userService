package org.example.service;

import org.example.entity.User;
import org.example.dao.UserDao;
import org.example.dao.UserDaoReal;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private final UserDao userDao = new UserDaoReal();
    private final Scanner scanner = new Scanner(System.in);

    public void createUser() {
        User user = new User();
        System.out.print("Имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Возраст: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));
        userDao.create(user);
    }

    public void viewUserById() {
        System.out.print("Введите ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userDao.readById(id);
        if (user != null) {
            System.out.println("ID: " + user.getId() + ", Name: " + user.getName()
                    + ", Email: " + user.getEmail() + ", Age: " + user.getAge()
                    + ", CreatedAt: " + user.getCreatedAt());
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    public void viewAllUsers() {
        List<User> users = userDao.readAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
        } else {
            users.forEach(u -> System.out.println("ID: " + u.getId() + ", Name: " + u.getName()
                    + ", Email: " + u.getEmail() + ", Age: " + u.getAge()
                    + ", CreatedAt: " + u.getCreatedAt()));
        }
    }

    public void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userDao.readById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Новое имя (" + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) user.setName(name);

        System.out.print("Новый email (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) user.setEmail(email);

        System.out.print("Новый возраст (" + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isBlank()) user.setAge(Integer.parseInt(ageInput));

        userDao.update(user);
    }

    public void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userDao.delete(id);
    }
}
