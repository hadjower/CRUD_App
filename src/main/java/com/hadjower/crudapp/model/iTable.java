package com.hadjower.crudapp.model;


import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface iTable {
    void add(User user);
    void edit(User user);
    void delete(User user);
    ObservableList<Note> getAll();

    void fillTestData();

    String getTableName();
    ArrayList<String> getColumnNamesAndTypes();
    String getDbName();

    ObservableList<String> getTableNames();
}
