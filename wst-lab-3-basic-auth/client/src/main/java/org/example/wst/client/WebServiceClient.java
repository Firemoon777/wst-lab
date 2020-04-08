package org.example.wst.client;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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
        CatWebService result = CatWebService.getCatWebServicePort();

        Map<String, Object> req_ctx = ((BindingProvider)result).getRequestContext();
        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url.toString());

        Map<String, List<String>> headers = new HashMap<>();

        String creds = "admin:123456";
        String base64 = Base64.getEncoder().encodeToString(creds.getBytes());
        headers.put("Authorization",  Collections.singletonList("Basic " + base64));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        return result;
    }

    public static void showAll() {
        CatWebService catWebService = getPort();
        List<Cat> cats = null;
        try {
            cats = catWebService.getCats();
        } catch (CatException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
            return;
        }
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
        Integer id = null;
        try {
            id = catWebService.create(name, age, breed, weight);
            System.out.println("Создан кот с id = " + id);
        } catch (CatException e) {
            System.out.println("Ошибка добавления: " + e.getMessage());
        }
    }

    public static void read() {
        CatWebService catWebService = getPort();
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        List<Cat> cats = null;
        try {
            cats = catWebService.read(id, name, age, breed, weight);
        } catch (CatException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
            return;
        }
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
        try {
            catWebService.update(id, name, age, breed, weight);
        } catch (CatException e) {
            System.out.println("Ошибка редактирования: " + e.getMessage());
        }
    }

    public static void delete() {
        CatWebService catWebService = getPort();
        Integer id = readInt("Идентификатор удалямой записи");
        try {
            catWebService.delete(id);
        } catch (CatException e) {
            System.out.println("Ошибка удаления: " + e.getMessage());
        }
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
