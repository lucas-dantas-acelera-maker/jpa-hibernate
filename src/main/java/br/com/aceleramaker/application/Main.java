package br.com.aceleramaker.application;

import br.com.aceleramaker.model.Product;
import br.com.aceleramaker.model.User;
import br.com.aceleramaker.repository.DAO;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=========JPA HIBERNATE=========");
            System.out.println("1 - User related operations");
            System.out.println("2 - Product related operations");
            String userIn = sc.nextLine();

            DAO<?> dao;
            switch (userIn) {
                case "1" -> dao = new DAO<>(User.class);
                case "2" -> dao = new DAO<>(Product.class);
                default -> throw new IllegalArgumentException("Please select a valid option.");
            }

            String entityName = dao.getEntityClass().getSimpleName();

            System.out.println("=========SELECT OPERATION=========");
            System.out.printf("1 - %s: Add new%n", entityName);
            System.out.printf("2 - %s: List all%n", entityName);
            userIn = sc.nextLine();

            Field[] fields = dao.getEntityClass().getDeclaredFields();

            String entityAttr1 = fields[1].getName();
            String entityAttr2 = fields[2].getName();

            switch (userIn) {
                case "1":
                    System.out.printf("%s %s: ", entityName, entityAttr1);
                    String name = sc.nextLine();
                    System.out.printf("%s %s: ", entityName, entityAttr2);
                    String in = sc.nextLine();

                    if (fields[2].getType() == Double.class) {
                        @SuppressWarnings("unchecked")
                        DAO<Product> productDAO = (DAO<Product>) dao;

                        double price = Double.parseDouble(in);
                        Product e = new Product(name, price);
                        productDAO.addEntity(e).close();
                    } else {
                        @SuppressWarnings("unchecked")
                        DAO<User> userDAO = (DAO<User>) dao;

                        User e = new User(name, in);
                        userDAO.addEntity(e).close();
                    }
                    break;
                case "2":
                    List<?> entities = dao.getEntities();
                    entities.forEach(System.out::println);
                    break;
                default:
                    throw new IllegalArgumentException("Please select a valid option.");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }
}