package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.DBTable;
import com.hadjower.crudapp.model.User;
import com.hadjower.crudapp.model.iTable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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


    private iTable table;
    private Parent edit;
    private FXMLLoader editLoader;
    private EditController editController;
    private Stage editStage;

    public void initialize() {
        table = new DBTable();
        initListeners();

        try {
            editLoader = new FXMLLoader();
            editLoader.setLocation(getClass().getResource("/fxml/edit.fxml"));
            edit = editLoader.load();
            editController = editLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));

        table.fillTestData();

        tableView.setItems(table.getAll());
    }

    private void initListeners() {
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    edit(event);
                }
            }
        });
    }


    public void add(ActionEvent actionEvent) {
        Window window = ((Node) actionEvent.getSource()).getScene().getWindow();
        editController.setUser(new User());
        openModalWindow("Creating new note", window);
        table.getAll().add(editController.getUser());

    }

    private void openModalWindow(String s, Window window) {
        if (editStage == null) {
            editStage = new Stage();
            Scene scene = new Scene(edit);
            editStage.setTitle(s);
            editStage.setScene(scene);
            editStage.setResizable(false);
            editStage.initOwner(window);
            editStage.initModality(Modality.WINDOW_MODAL);
        }

        editStage.showAndWait();
    }

    public void edit(Event mouseEvent) {
        Window window = ((Node) mouseEvent.getSource()).getScene().getWindow();

        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        editController.setUser(selectedUser);
        openModalWindow("Editing note", window);
    }

    public void delete(ActionEvent actionEvent) {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        table.delete(selectedUser);
    }

    public void update(ActionEvent actionEvent) {

    }
}
