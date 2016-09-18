package com.hadjower.crudapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBTable implements iTable, Connectable {

    private final String URL = "jdbc:sqlite:src/main/resources/db/test.db";
    private final String dbName = "test";
    private String tableName;
    private String primaryKey = "id";

    private Statement statement;
    private ResultSet res;
    private Connection conn;

    public void add(Note note) {
        String insert = createAddQuery(note);
        try {
            statement.execute(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String createAddQuery(Note note) {
        List<String> colNames = Note.getColumnNames();
        int paramsQuantity = colNames.size();
        StringBuilder insert = new StringBuilder("INSERT INTO " + tableName);
        insert.append(" (");
        for (int i = 1; i < paramsQuantity; i++) {
            insert.append(colNames.get(i));
            if (i != paramsQuantity - 1)
                insert.append(", ");
        }
        insert.append(") VALUES (");
        for (int i = 1; i < paramsQuantity; i++) {

            String value = note.getValue(colNames.get(i));

            //if column from list with column names and types contains TEXT e.g. String type
            if (getColumnNamesAndTypes().get(i).contains("TEXT") && value != null) {
                insert.append("'" + value + "'");
            } else {
                if (value.equals("")) {
                    insert.append("NULL");
                } else {
                    insert.append(value);
                }
            }

            if (i != paramsQuantity - 1)
                insert.append(", ");
        }
        insert.append(")");
        System.out.println(insert);
        return insert.toString();
    }

    public void edit(Note note) {
        String update = getUpdateQuery(note);

        try {
            statement.execute(update);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getUpdateQuery(Note note) {
        List<String> colNames = Note.getColumnNames();
        int paramsQuantity = colNames.size();

        StringBuilder update = new StringBuilder("UPDATE " + tableName);
        update.append(" SET ");

        for (int i = 1; i < paramsQuantity; i++) {
            update.append(colNames.get(i));
            update.append("=");

            String value = note.getValue(colNames.get(i));

            //if column from list with column names and types contains TEXT e.g. String type
            if (getColumnNamesAndTypes().get(i).contains("TEXT") && value != null) {
                update.append("'" + value + "'");
            } else {
                if (value.equals("")) {
                    update.append("NULL");
                } else {
                    update.append(value);
                }
            }

            if (i != paramsQuantity - 1)
                update.append(", ");
        }
        update.append(" WHERE ").append(primaryKey).append("=");
        update.append(note.getValue(primaryKey));

        return update.toString();
    }

    public void delete(Note note) {
        String delete = "DELETE FROM " + tableName + " WHERE " + primaryKey  + "=?";
        try {
            PreparedStatement ps = conn.prepareStatement(delete);
            ps.setInt(1, Integer.parseInt(note.getValue(primaryKey)));
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


    public String getTableName() {
        return tableName;
    }

    public ArrayList<String> getColumnNamesAndTypes() {
        try {
            res = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = res.getMetaData();
            ArrayList<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i) + " " + rsmd.getColumnTypeName(i));
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

    @Override
    public String getPrimaryKey() {
        return primaryKey;
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

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void createTable(String tableName, List<String> columnNames) {
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName);
        sql.append(" (");
        sql.append("id INTEGER PRIMARY KEY");
        for (String columnName : columnNames) {
            sql.append(", ");
            if (columnName.equals("id") || columnName.equals("_id")) {
                continue;
            }
            sql.append(columnName);
            sql.append(" TEXT");
        }
        sql.append(")");
        try {
            statement.execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTable(String tableName) {
        try {
            statement.execute("DROP TABLE " + tableName);
            this.tableName = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //todo after update infos
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
