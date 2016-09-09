package com.hadjower.crudapp.model;


import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public interface iTable {
    void add(User user);
    void edit(User user);
    void delete(User user);
    ObservableList<User> getAll();

    void fillTestData();
}
