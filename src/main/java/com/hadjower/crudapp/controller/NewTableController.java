package com.hadjower.crudapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewTableController {
    @FXML
    public Button okBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public TextField tableNameField;
    @FXML
    public TextField colNamesField;

    String tableName;
    private List<String> columnNames;

    public void click(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();

        if (object instanceof Button) {
            Button btn = (Button) object;

            switch (btn.getId()) {
                case "okBtn":
                    tableName = tableNameField.getText();
                    columnNames = splitColNames();
                    hideWindow(actionEvent);
                    break;
                case "cancelBtn":
                    tableName = null;
                    hideWindow(actionEvent);
                    break;
            }
        }
    }

    private List<String> splitColNames() {
        String[] names;
        names = colNamesField.getText().split(",");
        for (int i = 0; i < names.length; i++) {
            names[i].trim();
        }
        return Arrays.asList(names);
    }

    private void hideWindow(ActionEvent actionEvent) {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }
}
