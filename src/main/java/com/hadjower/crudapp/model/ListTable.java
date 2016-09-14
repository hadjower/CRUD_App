package com.hadjower.crudapp.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ListTable implements iTable {
    private ObservableList<User> list = FXCollections.observableArrayList();

    public void add(User user) {
        list.add(user);
    }

    public void edit(User user) {

    }

    public void delete(User user) {
        list.remove(user);
    }

    public ObservableList<User> getAll() {
        return list;
    }

    public void fillTestData() {
        add(new User(1, "John", 23));
        add(new User(2, "Vlad", 18));
        add(new User(3, "Ann", 25));
    }

    public String getTableName() {
        return null;
    }

    public ArrayList<String> getColumnNames() {
        return null;
    }

    public String getDbName() {
        return null;
    }

    public ObservableList<String> getTableNames() {
        return null;
    }
}
