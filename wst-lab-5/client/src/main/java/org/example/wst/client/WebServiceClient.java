package org.example.wst.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.example.wst.entity.Cat;

import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WebServiceClient {

    private static final String URL = "http://localhost:8080/rest/cats";

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

    public static void showAll() {
        Client client = new Client();
        WebResource resource = client.resource(URL);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Cat>> stations = new GenericType<List<Cat>>() {
        };

        List<Cat> cats = resource.accept(MediaType.APPLICATION_JSON).get(stations);
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

    private static WebResource setParamIfNotNull(WebResource resource, String paramName, Object value) {
        if (value == null) {
            return resource;
        }
        return resource.queryParam(paramName, value.toString());
    }

    public static void create() {
        System.out.println("Уточните запрос:");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        Cat cat = new Cat(null, name, age,breed, weight);

        Client client = new Client();
        WebResource resource = client.resource(URL);
        String body = resource.accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE)
                .entity(cat, MediaType.APPLICATION_JSON_TYPE)
                .post(String.class);
        System.out.println("Cоздана запись с id = " + body);
    }

    public static void read() {
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        Client client = new Client();
        WebResource resource = client.resource(URL);
        resource = setParamIfNotNull(resource, "id", id);
        resource = setParamIfNotNull(resource, "name", name);
        resource = setParamIfNotNull(resource, "age", age);
        resource = setParamIfNotNull(resource, "breed", breed);
        resource = setParamIfNotNull(resource, "weight", weight);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Cat>> stations = new GenericType<List<Cat>>() {
        };

        List<Cat> cats = resource.accept(MediaType.APPLICATION_JSON).get(stations);
        printCats(cats);
    }

    public static void update() {
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор");
        String name = readStr("Имя");
        Integer age = readInt("Возраст");
        String breed = readStr("Порода");
        Integer weight = readInt("Вес");
        Client client = new Client();
        WebResource resource = client.resource(URL);

        Cat cat = new Cat(null, name, age,breed, weight);
        String updateResponse = resource.path(String.valueOf(id))
                .entity(cat, MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .put(String.class);
        System.out.println("Обновлено");
    }

    public static void delete() {
        System.out.println("Уточните запрос:");
        Integer id = readInt("Идентификатор");
        Client client = new Client();
        WebResource resource = client.resource(URL);
        String body = resource.path(String.valueOf(id)).accept(MediaType.TEXT_PLAIN_TYPE).delete(String.class);
        System.out.println("Удалено");
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
