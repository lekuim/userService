package org.example.controller;

import org.example.config.HibernateUtil;
import org.example.service.UserService;

import java.util.Scanner;

public class Main {
    private static final UserService service = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> service.createUser();
                case "2" -> service.viewUserById();
                case "3" -> service.viewAllUsers();
                case "4" -> service.updateUser();
                case "5" -> service.deleteUser();
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


}
