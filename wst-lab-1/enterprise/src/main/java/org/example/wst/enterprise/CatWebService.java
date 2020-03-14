package org.example.wst.enterprise;

import org.example.wst.dao.CatDAO;
import org.example.wst.entity.Cat;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(serviceName = "CatWebService", targetNamespace = "http://0.0.0.0:8080/app")
public class CatWebService {
    @Resource
    private DataSource dataSource;

    private CatDAO catDAO;

    private Connection getConnection() {
        Connection result = null;
        try {
            result = dataSource.getConnection();
        } catch (SQLException e) {
            Logger.getLogger(CatWebService.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @WebMethod(operationName = "getCats")
    public List<Cat> getCats() {
        try {
            catDAO = new CatDAO(getConnection());
            return catDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "filter")
    public List<Cat> filter(@WebParam(name = "id")@XmlElement(nillable=true) Integer id,
                            @WebParam(name = "name")@XmlElement(nillable=true) String name,
                            @WebParam(name = "age")@XmlElement(nillable=true) Integer age,
                            @WebParam(name = "breed")@XmlElement(nillable=true) String breed,
                            @WebParam(name = "weight")@XmlElement(nillable=true) Integer weight) {
        try {
            catDAO = new CatDAO(getConnection());
            return catDAO.filter(id, name, age, breed, weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
