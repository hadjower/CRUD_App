package com.hadjower.crudapp.controller;

import com.hadjower.crudapp.model.*;
import com.hadjower.crudapp.model.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

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
    private Parent editRoot;
    private Parent newTableRoot;
    private EditController editController;
    private NewTableController newTableController;
    private Stage editStage;
    private Stage newTableStage;

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
        setTableInfo();
        if (editController != null) {
            editController.reset();
        }
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
        return columnNames.contains("id") ? (tableView.getPrefWidth() - 40) / (columnNames.size() - 1) : tableView.getPrefWidth() / columnNames.size();
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
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/edit_stage.fxml"));
            editRoot = fxmlLoader.load();
            editController = fxmlLoader.getController();
            editStage = new Stage();

            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/new_table.fxml"));
            newTableRoot = fxmlLoader.load();
            newTableController = fxmlLoader.getController();
            newTableStage = new Stage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTableInfo() {
        table_name.setText("Table " + table.getTableName()!=null ? table.getTableName() : "-----");

        if (table.getTableName() != null) {
            ObservableList<String> columnNames = FXCollections.observableArrayList(table.getColumnNamesAndTypes());
            tableInfoListView.setItems(columnNames);
            updateNotesCounter();
        } else {
            tableInfoListView.getItems().clear();
            notesCounter.setText("Amount of notes: -----");
        }
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

            if (tablesListView.getSelectionModel().getSelectedItem() != null) {
                newDelTableBtn.setText("Delete");
                openTableBtn.setDisable(false);
            } else {
                newDelTableBtn.setText("New");
                openTableBtn.setDisable(true);

            }
        });
    }

    public void add(ActionEvent actionEvent) {
        editController.setNote(new Note());
        openModalWindow("Creating new note", Window.EDIT);
        //if cancel wasn't pressed
        table.add(editController.getNote());
        update();
    }

    private void openModalWindow(String s, Window window) {
        Stage stage = null;
        Parent root = null;
        switch (window) {
            case EDIT:
                stage = editStage;
                root = editRoot;
                break;
            case NEW_TABLE:
                stage = newTableStage;
                root = newTableRoot;
                break;
        }
        if (root.getScene() == null) {
            Scene scene = new Scene(root);
            stage.setTitle(s);
            stage.setScene(scene);
            stage.initOwner(mainStage);
            stage.initModality(Modality.WINDOW_MODAL);
        }

        stage.showAndWait();
    }

    public void edit(Event mouseEvent) {
        Note selectedNote = (Note) tableView.getSelectionModel().getSelectedItem();
        if (selectedNote == null)
            return;
        editController.setNote(selectedNote);
        openModalWindow("Editing note", Window.EDIT);
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
                    String selectedItem = tablesListView.getSelectionModel().getSelectedItem();
                    tablesListView.getSelectionModel().clearSelection();
                    loadTable(selectedItem);
                    btn.setDisable(true);
                    break;
                case "newDelTableBtn":
                    if (btn.getText().equals("New")) {
                        openModalWindow("Creating new table", Window.NEW_TABLE);
                        if (newTableController.getTableName() != null) {
                            String tableName = newTableController.getTableName();
                            List<String> columnNames = newTableController.getColumnNames();
                            table.createTable(tableName, columnNames);
                            loadTable(tableName);
                            setDBInfo();
                        }
                    } else {
                        String tableName = tablesListView.getSelectionModel().getSelectedItem();
                        if (tableName != null) {
                            String currTable = table.getTableName();
                            table.deleteTable(tableName);
                            if (currTable.equals(tableName)) {
                                tableView.getColumns().clear();
                                setTableInfo();
                            }
                            setDBInfo();
                        }
                    }
                    break;
            }
        }
    }
}
