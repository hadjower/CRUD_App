package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.DBTable;
import com.hadjower.crudapp.model.User;
import com.hadjower.crudapp.model.iTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainStageController {
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn<User, Integer> idCol;
    @FXML
    public TableColumn<User, String> nameCol;
    @FXML
    public TableColumn<User, Integer> ageCol;


    iTable table;
    private Parent edit;

    public void initialize() {
        table = new DBTable();
        try {
            edit = FXMLLoader.load(getClass().getResource("/fxml/edit.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));

        table.fillTestData();

        tableView.setItems(table.getAll());
    }


    public void add(ActionEvent actionEvent) {
            Window window = ((Node)actionEvent.getSource()).getScene().getWindow();
        User selectedUser = (User)tableView.getSelectionModel().getSelectedItem();
            openModalWindow("Creating new note", window);

    }

    private void openModalWindow(String s, Window window){
        Stage editStage = new Stage();
        Scene scene = new Scene(edit);
        editStage.setTitle(s);
        editStage.setScene(scene);
        editStage.setResizable(false);
        editStage.initOwner(window);
        editStage.initModality(Modality.WINDOW_MODAL);

        editStage.show();
    }

    public void edit(ActionEvent actionEvent) {

    }

    public void delete(ActionEvent actionEvent) {

    }

    public void update(ActionEvent actionEvent) {

    }
}
