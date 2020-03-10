package org.example.wst.standalone;

import javax.xml.ws.Endpoint;

public class App {
    public static void main(String[] args) {
        String url = "http://0.0.0.0:8080/app/CatService";
        CatWebService catWebService = new CatWebService();
        Endpoint.publish(url, catWebService);
    }
}
