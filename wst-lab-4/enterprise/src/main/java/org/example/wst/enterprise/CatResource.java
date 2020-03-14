package org.example.wst.enterprise;

import org.example.wst.dao.CatDAO;
import org.example.wst.entity.Cat;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("/cats")
@Produces({MediaType.APPLICATION_JSON})
public class CatResource {
    @Resource(lookup = "jdbc/postgres")
    private DataSource dataSource;

    @GET
    public List<Cat> read(@QueryParam("id")     Integer id,
                          @QueryParam("name")   String  name,
                          @QueryParam("age")    Integer age,
                          @QueryParam("breed")  String  breed,
                          @QueryParam("weight") Integer weight) throws SQLException {
        CatDAO catDAO = new CatDAO();
        return catDAO.read(id, name, age, breed, weight);
    }

    private Connection getConnection() {
        Connection result = null;
        try {
            result = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CatResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
