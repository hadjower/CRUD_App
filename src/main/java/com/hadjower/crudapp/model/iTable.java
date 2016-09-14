package com.hadjower.crudapp.model;


import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface iTable {
    void add(User user);
    void edit(User user);
    void delete(User user);
    ObservableList<User> getAll();

    void fillTestData();

    String getTableName();
    ArrayList<String> getColumnNames();
    String getDbName();

    ObservableList<String> getTableNames();
}
