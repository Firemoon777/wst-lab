package org.example.wst.standalone;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.example.wst.dao.CatDAO;
import org.example.wst.dao.SimplePostgresSQLDAO;
import org.example.wst.entity.Cat;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "CatWebService", targetNamespace = "http://0.0.0.0:8080/app")
public class CatWebService {

    @Inject
    private CatDAO catDAO;

    @Resource
    WebServiceContext wsctx;

    @WebMethod(operationName = "getCats")
    public List<Cat> getCats() throws CatException {
        checkAuth();

        SimplePostgresSQLDAO dao = new SimplePostgresSQLDAO();
        List<Cat> cats = dao.getAllCats();
        return cats;
    }

    @WebMethod(operationName = "create")
    public Integer create(@WebParam(name = "name")   @XmlElement(nillable = true) String  name,
                          @WebParam(name = "age")    @XmlElement(nillable = true) Integer age,
                          @WebParam(name = "breed")  @XmlElement(nillable = true) String  breed,
                          @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) throws CatException {
        checkAuth();

        if(age < 0) {
            CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
            throw new CatException("Возраст не может быть меньше 0", catServiceFault);
        }
        if(weight < 0) {
            CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
            throw new CatException("Вес не может быть меньше 0", catServiceFault);
        }
        return catDAO.create(name, age, breed, weight);
    }

    @WebMethod(operationName = "read")
    public List<Cat> read(@WebParam(name = "id")     @XmlElement(nillable = true) Integer id,
                            @WebParam(name = "name")   @XmlElement(nillable = true) String  name,
                            @WebParam(name = "age")    @XmlElement(nillable = true) Integer age,
                            @WebParam(name = "breed")  @XmlElement(nillable = true) String  breed,
                            @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) throws CatException {
        checkAuth();

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
                          @WebParam(name = "weight") @XmlElement(nillable = true) Integer weight) throws CatException {
        checkAuth();

        try {
            if (catDAO.read(id, null, null, null, null).size() == 0) {
                CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
                throw new CatException("Нет объекта с таким id", catServiceFault);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(age < 0) {
            CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
            throw new CatException("Возраст не может быть меньше 0", catServiceFault);
        }
        if(weight < 0) {
            CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
            throw new CatException("Вес не может быть меньше 0", catServiceFault);
        }
        return catDAO.update(id, name, age, breed, weight);
    }

    @WebMethod(operationName = "delete")
    public Integer delete(@WebParam(name = "id")     @XmlElement(nillable = true) Integer id) throws CatException {
        checkAuth();

        try {
            if (catDAO.read(id, null, null, null, null).size() == 0) {
                CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
                throw new CatException("Нет объекта с таким id", catServiceFault);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catDAO.delete(id);
    }

    private void checkAuth() throws CatException {
        MessageContext mctx = wsctx.getMessageContext();

        //get detail from request headers
        Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);

        List authList = (List) http_headers.get("Authorization");

        if (authList == null) {
            throw new CatException("Нет заголовка авторизации", CatServiceFault.defaultInstance());
        }

        String header = authList.get(0).toString();
        String base64 = header.split(" ")[1];
        String[] creds = (new String(Base64.getDecoder().decode(base64))).split(":");

        for(int i = 0; i < creds.length; i++) {
            System.out.println("cred " + creds[i]);
        }

        String username = creds[0];
        String password = creds[1];

        //Should validate username and password with database
        if (!(username.equals("admin") && password.equals("123456"))) {
            CatServiceFault catServiceFault = CatServiceFault.defaultInstance();
            throw new CatException("Не авторизован", catServiceFault);
        }
    }

    public CatWebService(CatDAO catDAO) {
        this.catDAO = catDAO;
    }

    public CatWebService() {
        this.catDAO = new CatDAO();
    }
}
