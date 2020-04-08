package org.example.wst.standalone;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.example.wst.dao.CatDAO;
import org.example.wst.dao.SimplePostgresSQLDAO;
import org.example.wst.entity.Cat;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Path("/cats")
@Produces({MediaType.APPLICATION_JSON})
public class CatResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(Cat cat, @Context UriInfo uriInfo,
                           @HeaderParam("Authorization") String auth) throws CatException {
        checkAuth(auth);
        if(cat.getAge() < 0) {
            throw new CatException("Возраст не может быть меньше 0");
        }
        if(cat.getWeight() < 0) {
            throw new CatException("Вес не может быть меньше 0");
        }
        CatDAO catDAO = new CatDAO();
        long createdId = catDAO.create(cat.getName(), cat.getAge(), cat.getBreed(), cat.getWeight());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(createdId));
        return Response.created(builder.build()).entity(String.valueOf(createdId)).build();
    }

    @GET
    public List<Cat> read(@QueryParam("id")     Integer id,
                          @QueryParam("name")   String  name,
                          @QueryParam("age")    Integer age,
                          @QueryParam("breed")  String  breed,
                          @QueryParam("weight") Integer weight,
                          @HeaderParam("Authorization") String auth) throws SQLException, CatException {
        checkAuth(auth);
        CatDAO catDAO = new CatDAO();
        try {
            if (catDAO.read(id, null, null, null, null).size() == 0) {
                throw new CatException("Нет объекта с таким id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catDAO.read(id, name, age, breed, weight);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String delete(@PathParam("id") Integer id,
                         @HeaderParam("Authorization") String auth) throws CatException {
        checkAuth(auth);
        CatDAO catDAO = new CatDAO();
        return String.valueOf(catDAO.delete(id));
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String update(@PathParam("id") Integer updateId, Cat cat,
                         @HeaderParam("Authorization") String auth) throws CatException {
        checkAuth(auth);
        if(cat.getAge() < 0) {
            throw new CatException("Возраст не может быть меньше 0");
        }
        if(cat.getWeight() < 0) {
            throw new CatException("Вес не может быть меньше 0");
        }
        CatDAO catDAO = new CatDAO();
        try {
            if (catDAO.read(updateId, null, null, null, null).size() == 0) {
                throw new CatException("Нет объекта с таким id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(catDAO.update(updateId, cat.getName(), cat.getAge(), cat.getBreed(), cat.getWeight()));
    }

    private void checkAuth(String header) throws CatException {
        if(header == null)
            throw new CatException("Нет заголовков");
        String base64 = header.split(" ")[1];
        String[] creds = (new String(Base64.getDecoder().decode(base64))).split(":");

        String username = creds[0];
        String password = creds[1];

        //Should validate username and password with database
        if (!(username.equals("admin") && password.equals("123456"))) {
            throw new CatException("Не авторизован");
        }
    }

}
