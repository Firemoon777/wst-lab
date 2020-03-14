package org.example.wst.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WebServiceClient {

    public static void menu() {
        Scanner in = new Scanner(System.in);
        Integer decision;
        while (true) {
            System.out.println("");
            System.out.println("Выберите пункт:");
            System.out.println("1. Создать");
            System.out.println("2. Прочитать");
            System.out.println("3. Изменить");
            System.out.println("4. Удалить");
            System.out.println("5. Выйти");
            System.out.println("");
            System.out.print("Выбор: ");

            try {
                decision = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Неверный выбор");
                continue;
            }
            switch (decision) {
                case 0:
                    showAll();
                    break;
                case 1:
                    create();
                    break;
                case 2:
                    read();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор");
                    continue;
            }
        }
    }

    public static void printCats(List<Cat> cats) {
        if(cats.isEmpty()) {
            System.out.println("Котов не найдено");
        } else {
            for(Cat c : cats) {
                System.out.println(WebServiceClient.catToString(c));
            }
        }
        System.out.println("Всего: " + cats.size());
    }

    public static CatWebService getPort() {
        URL url = null;
        try {
            url = new URL("http://0.0.0.0:8080/app/CatWebService?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CatWebService_Service CatWebService = new CatWebService_Service(url);
        return CatWebService.getCatWebServicePort();
    }

    public static void showAll() {
        CatWebService catWebService = getPort();
        List<Cat> cats = catWebService.getCats();
        printCats(cats);
    }

    public static Integer readInt(String prompt) {
        Scanner in = new Scanner(System.in);
        System.out.print(prompt + ": ");
        String line = in.nextLine();
        if(line.length() == 0) {
            return null;
        }
        return Integer.valueOf(line);
    }

    public static String readStr(String prompt) {
        Scanner in = new Scanner(System.in);
        System.out.print(prompt + ": ");
        String line = in.nextLine();
        if(line.length() == 0) {
            return null;
        }
        return line;
    }

    public static void create() {
        CatWebService catWebService = getPort();
        System.out.println("Введите информацию о новом коте:");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        Integer id = catWebService.create(name, age, breed, weight);
        System.out.println("Создан кот с id = " + id);
    }

    public static void read() {
        CatWebService catWebService = getPort();
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        List<Cat> cats = catWebService.read(id, name, age, breed, weight);
        printCats(cats);
    }

    public static void update() {
        CatWebService catWebService = getPort();
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор редактируемой записи");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        catWebService.update(id, name, age, breed, weight);
    }

    public static void delete() {
        CatWebService catWebService = getPort();
        Integer id = readInt("Идентификатор удалямой записи");
        catWebService.delete(id);
    }

    public static void main(String[] args) {
        menu();
    }

    public static  String checkNull (String s) {
        return s.length()==0 ? null : s;
    }

    public static String catToString(Cat c) {
        return "Cat[\n" +
                "\tid = " + c.getId() + "\n" +
                "\tname = " + c.getName() + "\n" +
                "\tage = " + c.getAge() + "\n" +
                "\tbreed = " + c.getBreed() + "\n" +
                "\tweight = " + c.getWeight() + "\n" +
                "]";
    }
}
