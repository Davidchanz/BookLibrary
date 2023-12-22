package org.dbtest;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.dbtest.controller.ShelfsController;
import org.dbtest.model.Book;
import org.dbtest.service.BookService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class BookView2 extends VBox {
    private int bookWidth = 120;
    private int bookHeight = 150;
    private final BookService bs = BookService.getInstance();
    public Book book;
    public boolean selected = false;

    public BookView2(Book book) throws MalformedURLException {
        super();
        this.setAlignment(Pos.CENTER);
        this.book = book;
        //this.setPrefSize(bookWidth, bookHeight);
        File file;
        if(book.getCover() == null)
            file = new File("/home/katsitovlis/Documents/Project/JavaMaven/DBTest/src/main/resources/org/dbtest/images/default_cover.png");
        else
            file = new File(book.getCover());
        ImageView imageView = null;
        try {
            imageView = new ImageView(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //imageView.fitWidthProperty().bind(this.widthProperty().divide(1.2));
        imageView.setFitHeight(bookHeight);
        imageView.setFitWidth(bookWidth);
        imageView.setPreserveRatio(true);

        //Important otherwise button will wrap to text + graphic size (no resizing on scaling).
        //this.setMaxWidth(Double.MAX_VALUE);
        this.setOnMouseClicked((event -> {
            if(!ShelfsController.editMode) {
                openBook(event);
            }else{
                selectBook(event);
            }
        }));
        this.getChildren().add(imageView);
        Label label = new Label(book.getName());
        this.getChildren().add(label);
        label.setWrapText(true);
        label.setMinSize(bookWidth, bookHeight/2);
        label.setMaxSize(bookWidth, bookHeight/2);
        label.setStyle("-fx-background-color: rgba(0, 100, 100, 0); -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 16px;");
    }
    public void openBook(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            final Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    try {
                        Desktop.getDesktop().open(new File(book.getUrl()));
                    } catch (IOException e) {
                        System.err.println(e.toString());
                    }
                    return null;
                }
            };
            final Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void selectBook(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            selected = true;
            this.setStyle("-fx-background-color: rgba(0, 0, 200, 0.5);");
        } else if (event.getButton() == MouseButton.SECONDARY) {
            selected = false;
            this.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        }
    }
}
