package com.hadjower.crudapp.model;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class DBTable implements iTable, Connectable {

    private String URL = "jdbc:sqlite:src/main/resources/db/test.db";
    private Statement statement;
    private ResultSet res;
    private Connection conn;
    private String tableName = "users";

    public void add(User user) {
        try {
            String insert = "INSERT INTO " + tableName +" (name, age) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void edit(User user) {
        String update = "UPDATE " + tableName + " SET name=?, age=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.setInt(3, user.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(User user) {
        String delete = "DELETE FROM " + tableName + " WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(delete);
            ps.setInt(1, user.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getAll() {
        try {
            ObservableList<User> list = FXCollections.observableArrayList();
            res = statement.executeQuery("SELECT * FROM " + tableName);
            while (res.next()) {
                list.add(new User(res.getInt("id"), res.getString("name"), res.getInt("age")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fillTestData() {

    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL);
            statement = conn.createStatement();
            System.err.println("Database successfully opened");
        } catch (SQLException e) {
            System.err.println("Connection error");
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Failed to close connection");
            }
        }
    }


}
