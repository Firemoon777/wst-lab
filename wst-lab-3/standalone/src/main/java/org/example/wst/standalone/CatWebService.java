package org.example.wst.standalone;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.example.wst.dao.CatDAO;
import org.example.wst.dao.SimplePostgresSQLDAO;
import org.example.wst.entity.Cat;

import java.sql.SQLException;
import java.util.List;

@WebService(serviceName = "CatWebService", targetNamespace = "http://0.0.0.0:8080/app")
public class CatWebService {

    @Inject
    private CatDAO catDAO;

    @WebMethod(operationName = "getCats")
    public List<Cat> getCats() {
        SimplePostgresSQLDAO dao = new SimplePostgresSQLDAO();
        List<Cat> cats = dao.getAllCats();
        return cats;
    }

    @WebMethod(operationName = "create")
    public Integer create(@WebParam(name = "name")   @XmlElement(nillable = true) String  name,
                          @WebParam(name = "age")    @XmlElement(nillable = true) Integer age,
                          @WebParam(name = "breed")  @XmlElement(nillable = true) String  breed,
                          @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) {
        return catDAO.create(name, age, breed, weight);
    }

    @WebMethod(operationName = "read")
    public List<Cat> read(@WebParam(name = "id")     @XmlElement(nillable = true) Integer id,
                            @WebParam(name = "name")   @XmlElement(nillable = true) String  name,
                            @WebParam(name = "age")    @XmlElement(nillable = true) Integer age,
                            @WebParam(name = "breed")  @XmlElement(nillable = true) String  breed,
                            @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) {
        try {
            return catDAO.read(id, name, age, breed, weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @WebMethod(operationName = "update")
    public Integer update(@WebParam(name = "id")     @XmlElement(nillable = true) Integer id,
                          @WebParam(name = "name")   @XmlElement(nillable = true) String  name,
                          @WebParam(name = "age")    @XmlElement(nillable = true) Integer age,
                          @WebParam(name = "breed")  @XmlElement(nillable = true) String  breed,
                          @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) {
        return catDAO.update(id, name, age, breed, weight);
    }

    @WebMethod(operationName = "delete")
    public Integer delete(@WebParam(name = "id")     @XmlElement(nillable = true) Integer id) {
        return catDAO.delete(id);
    }

    public CatWebService(CatDAO catDAO) {
        this.catDAO = catDAO;
    }

    public CatWebService() {
        this.catDAO = new CatDAO();
    }
}
