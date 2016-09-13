package com.hadjower.crudapp;

import com.hadjower.crudapp.controller.MainStageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/main_stage.fxml"));
        Parent root = fxmlLoader.load();
        ((MainStageController)fxmlLoader.getController()).setMainStage(primaryStage);
        ((MainStageController)fxmlLoader.getController()).initListeners();
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setTitle("CRUD Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
