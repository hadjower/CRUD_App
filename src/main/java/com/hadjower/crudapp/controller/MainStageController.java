package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public ListView<String> tablesListView;
    @FXML
    public Label dbNameLabel;
    @FXML
    public Button openTableBtn;
    @FXML
    public Button newDelTableBtn;


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

        loadFxml();

        setDBInfo();
        setTableInfo();


//        tuneTable();
//        update();
    }

    private void loadTable(String tableName) {
        table.setTableName(tableName);
        tuneTable();
        update();
    }

    private void tuneTable() {
        tableView.getColumns().clear();
        setTableColumns();
    }


    private void setDBInfo() {
        dbNameLabel.setText("Current database: " + table.getDbName());
        tablesListView.setItems(table.getTableNames());
    }

    private void setTableColumns() {
        List<String> columnNames = getColumnNames();

        double width = calculateWidth(columnNames);

        Note.setColumnNames(columnNames);
        Note note = new Note();
        for (String columnName : columnNames) {
            note.setItem(columnName, "test");
            TableColumn<Note, String> column = new TableColumn<>(columnName);
            column.setMinWidth(columnName.equals(table.getPrimaryKey()) ? 40 : width);
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

        tablesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            openTableBtn.setDisable(false);

            if (tablesListView.getSelectionModel().getSelectedItem() != null) {
                newDelTableBtn.setText("Delete");
            } else {
                newDelTableBtn.setText("New");

            }
        });
    }

    public void add(ActionEvent actionEvent) {
        editController.setNote(new Note());
        openModalWindow("Creating new note");
        table.add(editController.getNote());
        update();
    }

    private void openModalWindow(String s) {
        if (editStage == null) {
            editStage = new Stage();
            Scene scene = new Scene(edit);
            editStage.setTitle(s);
            editStage.setScene(scene);
            editStage.initOwner(mainStage);
            editStage.initModality(Modality.WINDOW_MODAL);
        }

        editStage.showAndWait();
    }

    public void edit(Event mouseEvent) {
        Note selectedNote = (Note) tableView.getSelectionModel().getSelectedItem();
        if (selectedNote == null)
            return;
        editController.setNote(selectedNote);
        openModalWindow("Editing note");
        table.edit(selectedNote);
        update();
    }

    public void delete(ActionEvent actionEvent) {
        Note selectedNote = (Note) tableView.getSelectionModel().getSelectedItem();
        if (selectedNote == null)
            return;
        table.delete(selectedNote);
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

    public void click(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();

        if (object instanceof Button) {
            Button btn = (Button) object;

            switch (btn.getId()) {
                case "openTableBtn":
                    String tableName = tablesListView.getSelectionModel().getSelectedItem();
                    tablesListView.getSelectionModel().clearSelection();
                    loadTable(tableName);
                    btn.setDisable(true);
                    break;
                case "newDelTableBtn":
                    break;
            }
        }
    }
}
