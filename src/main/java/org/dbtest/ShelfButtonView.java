package org.dbtest;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.dbtest.controller.Shelf;
import org.dbtest.controller.ShelfsController;
import org.dbtest.model.Book;
import org.dbtest.service.BookService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;

public class ShelfButtonView extends JFXButton {
    private final BookService bs = BookService.getInstance();
    public org.dbtest.model.Shelf shelf;
    public ShelfButtonView(org.dbtest.model.Shelf shelf, ScrollPane booksPane) {
        super(shelf.getName());
        this.shelf = shelf;
        this.setPrefSize(179, 38);
        this.setFont(new Font(16));
        this.setAlignment(Pos.BASELINE_LEFT);
        this.setButtonType(ButtonType.FLAT);
        this.setStyle("-fx-background-color: rgb(60,60,60,1); -fx-text-fill: white");
        this.setOnAction((event) -> {
            openShelf(shelf, booksPane);
        });
        this.setOnMouseEntered(this::onHoverOn);
        this.setOnMouseExited(this::onHoverOff);
    }

    public void openShelf(org.dbtest.model.Shelf shelf, ScrollPane booksPane){
        Shelf shelfController = new Shelf(shelf);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Shelf.fxml"));
        fxmlLoader.setController(shelfController);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("shelf load error");
            throw new RuntimeException(e);
        }
        //booksPane.getChildren().clear();
        booksPane.setContent(shelfController.booksPane);
        ShelfsController.currentShelf = shelfController;

        ShelfsController controller;
        controller = (ShelfsController)booksPane.getUserData();
        controller.booksCount.setText(String.valueOf(shelfController.booksPane.getChildren().size()));
    }

    public void onHoverOn(MouseEvent event) {
        Button b = (Button)event.getSource();
        b.setStyle("-fx-background-color: rgba(80, 80, 80, 1); -fx-text-fill: white;");
    }

    public void onHoverOff(MouseEvent event) {
        Button b = (Button)event.getSource();
        b.setStyle("-fx-background-color: rgba(60, 60, 60, 1); -fx-text-fill: white;");
    }
}
