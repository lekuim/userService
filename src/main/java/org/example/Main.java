package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDaoReal();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createUser();
                case "2" -> viewUserById();
                case "3" -> viewAllUsers();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== User Service Menu ===");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Посмотреть пользователя по ID");
        System.out.println("3. Посмотреть всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void createUser() {
        User user = new User();
        System.out.print("Имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Возраст: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));
        userDao.create(user);
    }

    private static void viewUserById() {
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

    private static void viewAllUsers() {
        List<User> users = userDao.readAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
        } else {
            users.forEach(u -> System.out.println("ID: " + u.getId() + ", Name: " + u.getName()
                    + ", Email: " + u.getEmail() + ", Age: " + u.getAge()
                    + ", CreatedAt: " + u.getCreatedAt()));
        }
    }

    private static void updateUser() {
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

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userDao.delete(id);
    }
}
