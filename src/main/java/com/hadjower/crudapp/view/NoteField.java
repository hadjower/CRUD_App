package com.hadjower.crudapp.view;


import com.hadjower.crudapp.controller.NoteFieldController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NoteField {
    private static final String PATH = "/fxml/note_field.fxml";
    private AnchorPane pane;
    private NoteFieldController controller;

    public NoteField() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(PATH));
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = fxmlLoader.getController();
    }

    public AnchorPane getPane() {
        return pane;
    }

    public String getValue() {
        return controller.getText();
    }

    public void setLabelText(String text) {
        controller.setName(text);
    }

    public void setTextFieldText(String text) {
        controller.setTextFieldText(text);
    }

}
