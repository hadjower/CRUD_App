package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    @FXML
    public Label table_name;
    @FXML
    public ListView tableInfoListView;
    @FXML
    public Label notesCounter;
    @FXML
    public ListView tablesListView;
    @FXML
    public Label dbNameLabel;


    private iTable table;
    private Connectable connectable;

    private Stage mainStage;
    private Parent edit;
    private FXMLLoader editLoader;
    private EditController editController;
    private Stage editStage;

    public void initialize() {
        connectable = new DBTable();
        connectable.connect();
        table = (iTable) connectable;
//        table = new ListTable();
//        initListeners();

        loadFxml();

        idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));

//        table.fillTestData();
        setDBInfo();
        setTableInfo();

        update();
    }

    private void setDBInfo() {
        dbNameLabel.setText("Current database: " + table.getDbName());
        tablesListView.setItems(table.getTableNames());
    }

    private void loadFxml() {
        try {
            editLoader = new FXMLLoader();
            editLoader.setLocation(getClass().getResource("/fxml/edit.fxml"));
            edit = editLoader.load();
            editController = editLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTableInfo() {
        table_name.setText("Table " + table.getTableName());
        ObservableList<String> columnNames = FXCollections.observableArrayList(table.getColumnNames());
        tableInfoListView.setItems(columnNames);
        updateNotesCounter();
    }

    private void updateNotesCounter() {
        notesCounter.setText("Amount of notes: " + table.getAll().size());
    }

    private void update() {
        tableView.refresh();
        tableView.setItems(table.getAll());
        updateNotesCounter();
    }

    public void initListeners() {
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    edit(event);
                }
            }
        });
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                connectable.closeConnection();
            }
        });
    }


    public void add(ActionEvent actionEvent) {
        editController.setUser(new User());
        openModalWindow("Creating new note");
        table.add(editController.getUser());
        update();
    }

    private void openModalWindow(String s) {
        if (editStage == null) {
            editStage = new Stage();
            Scene scene = new Scene(edit);
            editStage.setTitle(s);
            editStage.setScene(scene);
            editStage.setResizable(false);
            editStage.initOwner(mainStage);
            editStage.initModality(Modality.WINDOW_MODAL);
        }

        editStage.showAndWait();
    }

    public void edit(Event mouseEvent) {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null)
            return;
        editController.setUser(selectedUser);
        openModalWindow("Editing note");
        table.edit(selectedUser);
        update();
    }

    public void delete(ActionEvent actionEvent) {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null)
            return;
        table.delete(selectedUser);
        update();
    }

    public void update(ActionEvent actionEvent) {
        update();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void exit(ActionEvent actionEvent) {
        connectable.closeConnection();
        System.exit(0);
    }
}
