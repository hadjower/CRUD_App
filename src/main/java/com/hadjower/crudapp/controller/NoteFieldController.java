package com.hadjower.crudapp.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NoteFieldController {
    @FXML
    private Label nameLabel;
    @FXML
    private TextField textField;

    public String getText() {
        return textField.getText();
    }

    public void setName(String string) {
        nameLabel.setText(string);
    }

    public void setTextFieldText(String text) {
        if (text != null) {
            textField.setText(text);
        }
    }
}
