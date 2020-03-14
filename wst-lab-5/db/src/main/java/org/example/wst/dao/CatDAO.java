package org.example.wst.dao;

import org.example.wst.entity.Cat;
import org.example.wst.query.BuildQuery;
import org.example.wst.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CatDAO {
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

    public Integer create(String name, Integer age, String breed, Integer weight) {
        try {
            connection.setAutoCommit(false);
            Integer newId;
            try (Statement idStatement = connection.createStatement()) {
                idStatement.execute("SELECT nextval('cat_id_seq') nextval");
                try (ResultSet rs = idStatement.getResultSet()) {
                    rs.next();
                    newId = rs.getInt("nextval");
                }
            }
            try (PreparedStatement stmnt = connection.prepareStatement("INSERT INTO cats(id, name,  age, breed, weight) VALUES (?, ?, ?, ?, ?)")) {
                stmnt.setInt(1, newId);
                stmnt.setString(2, name);
                stmnt.setInt(3, age);
                stmnt.setString(4, breed);
                stmnt.setInt(5, weight);
                int count = stmnt.executeUpdate();
                if (count == 0) {
                    throw new RuntimeException("Could not execute query");
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return newId;
        } catch (SQLException ex) {
            Logger.getLogger(CatDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public List<Cat> read(Integer id, String name, Integer age, String breed, Integer weight) throws SQLException {

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

    public int update(Integer id, String name, Integer age, String breed, Integer weight) {
        try {
            connection.setAutoCommit(true);
            String updateStr = "UPDATE cats SET name = ?, age = ?, breed = ?, weight = ? WHERE id = ?";
            try (PreparedStatement stmnt = connection.prepareStatement(updateStr)) {
                stmnt.setString(1, name);
                stmnt.setInt(2, age);
                stmnt.setString(3, breed);
                stmnt.setInt(4, weight);
                stmnt.setInt(5, id);
                int updated = stmnt.executeUpdate();
                return updated;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CatDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int delete(Integer id) {
        try {
            connection.setAutoCommit(true);
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM cats WHERE id = ?")) {
                ps.setInt(1, id);
                return ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CatDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
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
