package org.example.wst.dao;

import org.example.wst.entity.Cat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimplePostgresSQLDAO {

    private Connection connection;

    public SimplePostgresSQLDAO(Connection connection) {
        this.connection = connection;
    }

    public SimplePostgresSQLDAO() {
        this.connection = ConnectionUtil.getConnection();

    }
    public static Cat getCatInfo(ResultSet rs) {
        Cat cat;

        try {
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            Integer age = rs.getInt("age");
            String breed = rs.getString("breed");
            Integer weight = rs.getInt("weight");

            return new Cat(id, name, age, breed, weight);
        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static List<Cat> getStationsByQuery(Connection connection, String query) {
        List<Cat> cats = new ArrayList<Cat>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Cat cat = SimplePostgresSQLDAO.getCatInfo(rs);
                cats.add(cat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cats;
    }


    public List<Cat> getAllCats() {
        return SimplePostgresSQLDAO.getStationsByQuery(connection, "SELECT * FROM cats");
    }

    public Cat getCatByName(String name) {
        Cat cat = SimplePostgresSQLDAO.getStationsByQuery(connection, "SELECT * FROM cats WHERE name=" + name).get(0);
        return cat;
    }

    public List<Cat> getStationsBySmth(String parameters) {
        return SimplePostgresSQLDAO.getStationsByQuery(connection, "SELECT * FROM cats where " + parameters + "=" + parameters);

    }

}
