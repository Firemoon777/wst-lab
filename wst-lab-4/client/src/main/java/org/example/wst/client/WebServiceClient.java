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
            System.out.println("1. Вывести всех котов");
            System.out.println("2. Поискать котов по критерию");
            System.out.println("3. Выйти");
            System.out.println("");
            System.out.print("Выбор: ");

            try {
                decision = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Неверный выбор");
                continue;
            }
            switch (decision) {
                case 1:
                    showAll();
                    break;
                case 2:
                    showFiltered();
                    break;
                case 3:
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

    public static void showFiltered() {
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
