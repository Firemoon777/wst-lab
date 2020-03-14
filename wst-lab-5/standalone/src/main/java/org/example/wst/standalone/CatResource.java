package org.example.wst.standalone;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.example.wst.dao.CatDAO;
import org.example.wst.dao.SimplePostgresSQLDAO;
import org.example.wst.entity.Cat;

import java.sql.SQLException;
import java.util.List;

@Path("/cats")
@Produces({MediaType.APPLICATION_JSON})
public class CatResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(Cat cat, @Context UriInfo uriInfo) {
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
                          @QueryParam("weight") Integer weight) throws SQLException {
        CatDAO catDAO = new CatDAO();
        return catDAO.read(id, name, age, breed, weight);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String delete(@PathParam("id") Integer id) {
        CatDAO catDAO = new CatDAO();
        return String.valueOf(catDAO.delete(id));
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String update(@PathParam("id") Integer updateId, Cat cat) {
        CatDAO catDAO = new CatDAO();
        return String.valueOf(catDAO.update(updateId, cat.getName(), cat.getAge(), cat.getBreed(), cat.getWeight()));
    }
}
