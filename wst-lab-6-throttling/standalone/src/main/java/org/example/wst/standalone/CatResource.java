package org.example.wst.standalone;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.example.wst.dao.CatDAO;
import org.example.wst.dao.SimplePostgresSQLDAO;
import org.example.wst.entity.Cat;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Semaphore;

@Path("/cats")
@Singleton
@Resource
@Produces({MediaType.APPLICATION_JSON})
public class CatResource {

    private Semaphore semaphore = new Semaphore(1);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(Cat cat, @Context UriInfo uriInfo) throws CatException, ThrottlingException {
        if(this.semaphore.tryAcquire() == false) {
            throw new ThrottlingException();
        }
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
        this.semaphore.release();
        return Response.created(builder.build()).entity(String.valueOf(createdId)).build();
    }

    @GET
    public List<Cat> read(@QueryParam("id")     Integer id,
                          @QueryParam("name")   String  name,
                          @QueryParam("age")    Integer age,
                          @QueryParam("breed")  String  breed,
                          @QueryParam("weight") Integer weight) throws SQLException, CatException, ThrottlingException, InterruptedException {
        if(this.semaphore.tryAcquire() == false) {
            throw new ThrottlingException();
        }
        CatDAO catDAO = new CatDAO();
        try {
            if (catDAO.read(id, null, null, null, null).size() == 0) {
                throw new CatException("Нет объекта с таким id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);
        this.semaphore.release();
        return catDAO.read(id, name, age, breed, weight);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String delete(@PathParam("id") Integer id) throws ThrottlingException {
        if(this.semaphore.tryAcquire() == false) {
            throw new ThrottlingException();
        }
        CatDAO catDAO = new CatDAO();
        this.semaphore.release();
        return String.valueOf(catDAO.delete(id));
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String update(@PathParam("id") Integer updateId, Cat cat) throws CatException, ThrottlingException {
        if(this.semaphore.tryAcquire() == false) {
            throw new ThrottlingException();
        }
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
        this.semaphore.release();
        return String.valueOf(catDAO.update(updateId, cat.getName(), cat.getAge(), cat.getBreed(), cat.getWeight()));
    }
}
