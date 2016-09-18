package com.hadjower.crudapp.model;


import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public interface iTable {
    void add(Note note);
    void edit(Note note);
    void delete(Note note);
    ObservableList<Note> getAll();

    String getTableName();
    ArrayList<String> getColumnNamesAndTypes();
    String getDbName();
    String getPrimaryKey();

    ObservableList<String> getTableNames();

    void setTableName(String tableName);

    void createTable(String tableName, List<String> columnNames);

    void deleteTable(String tableName);
}
