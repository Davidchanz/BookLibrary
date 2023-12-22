package org.dbtest;

import jakarta.persistence.criteria.Root;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    double x,y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
        Parent root = fxmlLoader.load();

        /*root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });*/
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Library");
        stage.setMinHeight(500);
        stage.setMinWidth(500);
        stage.show();
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");

        launch();
    }
}