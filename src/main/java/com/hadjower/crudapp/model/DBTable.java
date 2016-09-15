package com.hadjower.crudapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;


public class DBTable implements iTable, Connectable {

    private String URL = "jdbc:sqlite:src/main/resources/db/test.db";
    private String dbName = "test";
    private String tableName = "users";

    private Statement statement;
    private ResultSet res;
    private Connection conn;

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

    public ObservableList<Note> getAll() {
        try {
            ObservableList<Note> notes = FXCollections.observableArrayList();
            res = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = res.getMetaData();
            while (res.next()) {
//                notes.add(new User(res.getInt("id"), res.getString("name"), res.getInt("age")));
                Note note = new Note();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    switch (rsmd.getColumnTypeName(i)) {
                        case "INTEGER":
                            note.setItem(rsmd.getColumnName(i), res.getInt(i));
                            break;
                        case "TEXT":
                            note.setItem(rsmd.getColumnName(i), res.getString(i));
                            break;
                    }
                }
                notes.add(note);
            }
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fillTestData() {

    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<String> getColumnNamesAndTypes() {
        try {
//            res = statement.executeQuery("SELECT sql FROM sqlite_master WHERE tbl_name = 'users' AND TYPE = 'column'");
            res = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = res.getMetaData();
            ArrayList<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i) + " " + rsmd.getColumnTypeName(i));
//                System.out.println(columnNames.get(i - 1));
            }
            return columnNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDbName() {
        return dbName;
    }

    public ObservableList<String> getTableNames() {
        try {
            ArrayList<String> tableNames = new ArrayList<>();
            res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            while (res.next()) {
                tableNames.add(res.getString("name"));
            }
            return FXCollections.observableArrayList(tableNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL);
            statement = conn.createStatement();
//            System.err.println("Database successfully opened");
        } catch (SQLException e) {
//            System.err.println("Connection error");
            e.printStackTrace();
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
