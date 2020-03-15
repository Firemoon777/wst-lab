package org.example.wst.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.transport.TransportException;
import org.uddi.api_v3.*;

import javax.xml.ws.BindingProvider;

public class WebServiceClient {

    private static CatWebService catWebService;

    public static void menu() {
        catWebService = getPort();

        Scanner in = new Scanner(System.in);
        Integer decision;
        while (true) {
            System.out.println("");
            System.out.println("Выберите пункт:");
            System.out.println("1. Создать");
            System.out.println("2. Прочитать");
            System.out.println("3. Изменить");
            System.out.println("4. Удалить");
            System.out.println("5. Бизнес");
            System.out.println("6. Выйти");
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
                    try {
                        bussinessMenu();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
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
            url = new URL("http://0.0.0.0:8090/app/CatWebService?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CatWebService_Service CatWebService = new CatWebService_Service(url);
        return CatWebService.getCatWebServicePort();
    }

    public static void showAll() {
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

    private static JuddiClient juddiClient;

    public static void bussinessMenu() throws RemoteException {
        Scanner in = new Scanner(System.in);

        String username = readStr("Имя пользователя UDDI");
        String password = readStr("Пароль");

        juddiClient = null;
        try {
            juddiClient = new JuddiClient("META-INF/uddi.xml");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
            return;
        }
        try {
            juddiClient.authenticate(username, password);
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        Integer decision;
        while (true) {
            System.out.println("");
            System.out.println("Выберите пункт:");
            System.out.println("1. Вывести все бизнесы");
            System.out.println("2. Зарегистрировать бизнес");
            System.out.println("3. Зарегистрировать сервис");
            System.out.println("4. Найти и использовать сервис");
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
                case 1:
                    listBusinesses();
                    break;
                case 2:
                    String name = readStr("Имя бизнеса");
                    createBusiness(name);
                    break;
                case 3:
                    String bbk = readStr("Ключ бизнеса");
                    String ssn = readStr("Имя сервиса");
                    String ssurl = readStr("Ссылка на wsdl");
                    try {
                        createService(bbk, ssn, ssurl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    String ffsn = readStr("Поиск по имени");
                    filterServices(ffsn);
                    String kkey = readStr("Ключ сервиса");
                    useService(kkey);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор");
                    continue;
            }
        }
    }

    private static void listBusinesses() throws RemoteException {
        JuddiUtil.printBusinessInfo(juddiClient.getBusinessList().getBusinessInfos());
    }

    public static void createBusiness(String businessName) throws RemoteException {
        businessName = businessName.trim();
        BusinessDetail business = juddiClient.createBusiness(businessName);
        System.out.println("Бизнес создан");
        for (BusinessEntity businessEntity : business.getBusinessEntity()) {
            System.out.printf("Ключ: '%s'\n", businessEntity.getBusinessKey());
            System.out.printf("Имя: '%s'\n", businessEntity.getName().stream().map(Name::getValue).collect(Collectors.joining(" ")));
        }
    }

    private static void createService(String businessKey, String serviceName, String wsdlUrl) throws Exception {
        List<ServiceDetail> serviceDetails = juddiClient.publishUrl(businessKey.trim(), serviceName.trim(), wsdlUrl.trim());
        System.out.printf("Сервисы, полученные по wsdl: %s\n", wsdlUrl);
        JuddiUtil.printServicesInfo(serviceDetails.stream()
                .map(ServiceDetail::getBusinessService)
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );
    }

    private static void filterServices(String filterArg) throws RemoteException {
        List<BusinessService> services = juddiClient.getServices(filterArg);
        JuddiUtil.printServicesInfo(services);
    }

    public static void changeEndpointUrl(String endpointUrl) {
        ((BindingProvider) catWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl.trim());
    }

    private static void useService(String serviceKey) throws RemoteException {

        ServiceDetail serviceDetail = juddiClient.getService(serviceKey.trim());
        if (serviceDetail == null || serviceDetail.getBusinessService() == null || serviceDetail.getBusinessService().isEmpty()) {
            System.out.printf("Сервис с ключом '%s' не найден\b", serviceKey);
            return;
        }
        List<BusinessService> services = serviceDetail.getBusinessService();
        BusinessService businessService = services.get(0);
        BindingTemplates bindingTemplates = businessService.getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            System.out.printf("Не найден шаблон для сервиса '%s' '%s'\n", serviceKey, businessService.getBusinessKey());
            return;
        }
        for (BindingTemplate bindingTemplate : bindingTemplates.getBindingTemplate()) {
            AccessPoint accessPoint = bindingTemplate.getAccessPoint();
            if (accessPoint.getUseType().equals(AccessPointType.END_POINT.toString())) {
                String value = accessPoint.getValue();
                System.out.printf("Применён эндпонт '%s'\n", value);
                changeEndpointUrl(value);
                return;
            }
        }
        System.out.printf("No endpoint found for service '%s'\n", serviceKey);
    }
}
