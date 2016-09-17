package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.Note;
import com.hadjower.crudapp.model.User;
import com.hadjower.crudapp.view.NoteField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class EditController {

    @FXML
    private VBox notesVBox;

    private Note note;
    private List<NoteField> fields;


    public void cancel(ActionEvent actionEvent) {
        hideWindow(actionEvent);
    }


    public void setNote(Note note) {
        this.note = note;
        int fieldCounter = Note.getColumnNames().size();

        //todo after changing tables set fields to null
        if (fields == null) {
            fields = new ArrayList<>(fieldCounter - 1);
            for (int i = 1; i < fieldCounter; i++) {
                NoteField noteField = new NoteField();
                noteField.setLabelText(Note.getColumnNames().get(i));
                fields.add(noteField);
                notesVBox.getChildren().add(noteField.getPane());
            }
        }

        for (int i = 0; i < fieldCounter - 1; i++) {
            fields.get(i).setTextFieldText(note.getValue(Note.getColumnNames().get(i+1)));
        }
    }

    private void hideWindow(ActionEvent actionEvent) {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void complete(ActionEvent actionEvent) {
        for (int i = 0; i < fields.size(); i++) {
            note.setItem(Note.getColumnNames().get(i+1), fields.get(i).getValue());
        }

        hideWindow(actionEvent);
    }

    public Note getNote() {
        return note;
    }
}
