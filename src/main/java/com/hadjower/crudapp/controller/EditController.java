package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditController {

    @FXML
    public TextField ageField;
    @FXML
    public TextField nameField;

    private User user;

    public void cancel(ActionEvent actionEvent) {
        hideWindow(actionEvent);
    }

    public void setUser(User user) {
        this.user = user;
        nameField.setText(user.getName());
        ageField.setText(String.valueOf(user.getAge()));
    }

    private void hideWindow(ActionEvent actionEvent) {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void complete(ActionEvent actionEvent) {
        user.setName(nameField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        hideWindow(actionEvent);
    }
}
