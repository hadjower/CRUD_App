package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        tuneTable();
//        table.fillTestData();
        setDBInfo();
        setTableInfo();

        update();
    }

    private void tuneTable() {
//        idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
//        nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
//        ageCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));
        tableView.getColumns().clear();
        setTableColumns();

//        setNotes();
    }

    private void setDBInfo() {
        dbNameLabel.setText("Current database: " + table.getDbName());
        tablesListView.setItems(table.getTableNames());
    }

    private void setNotes() {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Note note = new Note();
            for (String columnName : Note.getColumnNames()) {
                note.setItem(columnName, "test" + i);
            }
            notes.add(note);
        }
        tableView.setItems(FXCollections.observableArrayList(notes));
    }

    private void setTableColumns() {
        List<String> columnNames = getColumnNames();

        double width = calculateWidth(columnNames);

        Note.setColumnNames(columnNames);
        Note note = new Note();
        for (String columnName : columnNames) {
            note.setItem(columnName, "test");
            TableColumn<Note, String> column = new TableColumn<>(columnName);
            column.setMinWidth(columnName.equals("id") ? 40 : width);
            column.setCellValueFactory(cd -> cd.getValue().getItems().get(columnName));
            tableView.getColumns().add(column);
        }
    }

    private double calculateWidth(List<String> columnNames) {
        return columnNames.contains("id") ? (tableView.getPrefWidth() - 40)/(columnNames.size() - 1) : tableView.getPrefWidth()/columnNames.size();
    }

    private List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        List<String> temp = table.getColumnNamesAndTypes();

        for (int i = 0; i < temp.size(); i++) {
            String str = temp.get(i);
            columnNames.add(str.contains(" ") ? str.split(" ")[0] : str);
        }
        return columnNames;
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
        ObservableList<String> columnNames = FXCollections.observableArrayList(table.getColumnNamesAndTypes());
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
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                edit(event);
            }
        });
        mainStage.setOnCloseRequest(event -> connectable.closeConnection());
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
