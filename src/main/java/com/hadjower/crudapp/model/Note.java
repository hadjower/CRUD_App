package com.hadjower.crudapp.model;


import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.List;

public class Note {
    private static List<String> columnNames;
    private HashMap<String, SimpleStringProperty> items;

    public Note() {
        items = new HashMap<>();
    }

    public static void setColumnNames(List<String> names) {
        columnNames = names;
    }

    public static List<String> getColumnNames() {
        return columnNames;
    }

    public HashMap<String, SimpleStringProperty> getItems() {
        return items;
    }

    public void setItem(String key, Object value) {
        if (value instanceof String)
            items.put(key, new SimpleStringProperty((String) value));
        if (value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Boolean)
            items.put(key, new SimpleStringProperty(String.valueOf(value)));
    }

    public String getValue(String key) {
        if (items.containsKey(key)) {
            return items.get(key).get();
        }
        return null;
    }
}
