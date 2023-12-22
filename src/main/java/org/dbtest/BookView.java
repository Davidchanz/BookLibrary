package org.dbtest;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import org.dbtest.model.Book;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class BookView extends ImageView {
    private int bookWidth = 120;
    private int bookHeight = 150;
    public BookView(Book book) throws MalformedURLException {
        super(new File(book.getCover()).toURI().toURL().toExternalForm());
        //this.setPrefSize(bookWidth, bookHeight);
        /*File file = new File(cover);
        ImageView imageView = null;
        try {
            this(file.toURI().toURL().toExternalForm());
            imageView = new ImageView(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.setGraphic(imageView);
        this.setContentDisplay(ContentDisplay.TOP);*/
        //imageView.fitWidthProperty().bind(this.widthProperty().divide(1.2));
        this.setFitHeight(bookHeight);
        this.setFitWidth(bookWidth);
        this.setPreserveRatio(true);
        //this.setStyle("-fx-background-radius: 40");
        //Important otherwise button will wrap to text + graphic size (no resizing on scaling).
        //this.setMaxWidth(Double.MAX_VALUE);
        this.setOnMouseClicked((event) -> {
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
        });
        setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 40; -fx-text-fill: white; -fx-font-size: 16px;");
    }
    /*public BookView(String cover){
        super();
        ImageView imageView = new ImageView(getClass().getResource(cover).toExternalForm());
        this.setGraphic(imageView);
    }*/
}
