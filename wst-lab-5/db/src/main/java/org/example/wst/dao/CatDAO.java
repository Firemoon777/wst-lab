package org.example.wst.dao;

import org.example.wst.entity.Cat;
import org.example.wst.query.BuildQuery;
import org.example.wst.query.Query;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@XmlRootElement
public class CatDAO implements Serializable {
    private final String TABLE_NAME = "cats";
    private final String ID_COLUMN = "id";
    private final String NAME_COLUMN = "name";
    private final String AGE_COLUMN = "age";
    private final String BREED_COLUMN = "breed";
    private final String WEIGHT_COLUMN = "weight";

    private final Connection connection;

    public CatDAO(Connection connection) {
        this.connection = connection;
    }

    public CatDAO() {
        this.connection = ConnectionUtil.getConnection();
    }

    public List<Cat> findAll() throws SQLException {
//        log.debug("Find all query");
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT *  FROM cats");
            List<Cat> result = rsToEntities(statement.getResultSet());
            System.out.println(result);
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public List<Cat> read(Integer id,  String name, Integer age, String breed, Integer weight) throws SQLException {

//        Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, name+city+line+type);

        if (Stream.of(id, name, age, breed, weight).allMatch(Objects::isNull)) {
            return findAll();
        }
        Query query = new BuildQuery()
                .tableName(TABLE_NAME)
                .selectColumns(ID_COLUMN, NAME_COLUMN, AGE_COLUMN, BREED_COLUMN, WEIGHT_COLUMN)
                .condition(new Condition(ID_COLUMN, id, Integer.class))
                .condition(new Condition(NAME_COLUMN, name, String.class))
                .condition(new Condition(AGE_COLUMN, age, Integer.class))
                .condition(new Condition(BREED_COLUMN, breed, String.class))
                .condition(new Condition(WEIGHT_COLUMN, weight, Integer.class))
                .buildPreparedStatementQuery();


        try {
            PreparedStatement ps = connection.prepareStatement(query.getQueryString());
            query.initPreparedStatement(ps);
            ResultSet rs = ps.executeQuery();
            return rsToEntities(rs);
        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }



    private List<Cat> rsToEntities(ResultSet rs) throws SQLException {
        List<Cat> result = new ArrayList<>();
        while (rs.next()) {
            result.add(resultSetToEntity(rs));
        }
        return result;
    }

    private Cat resultSetToEntity(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        Integer age = rs.getInt("age");
        String breed = rs.getString("breed");
        Integer weight = rs.getInt("weight");

        return new Cat(id, name, age, breed, weight);

    }
}
