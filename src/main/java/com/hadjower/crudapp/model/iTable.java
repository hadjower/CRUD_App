package com.hadjower.crudapp.model;


import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface iTable {
    void add(Note note);
    void edit(Note note);
    void delete(Note note);
    ObservableList<Note> getAll();

    String getTableName();
    ArrayList<String> getColumnNamesAndTypes();
    String getDbName();

    ObservableList<String> getTableNames();
}
