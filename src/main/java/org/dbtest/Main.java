package org.dbtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");

        /*if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("src/main/resources/1.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }*/


        launch();
    }
}