package org.dbtest.controller;

import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.dbtest.BookView;
import org.dbtest.BookView2;
import org.dbtest.Main;
import org.dbtest.service.BookService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class BigPictureController implements Initializable {
    public GridPane bookGrid;
    @FXML
    public VBox content;
    public ScrollPane scrolPane;
    public VBox menu;
    public HBox main;
    private Stage stage;
    private Scene scene;
    private final BookService bs = BookService.getInstance();
    private int width;
    private int height;
    public void TableModeOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("scene2 load error");
            throw new RuntimeException(e);
        }
        stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
        /*menu.setStyle("-fx-background-color: rgba(0, 0, 110, 0)");
        bookGrid.setStyle("-fx-background-color: rgba(0, 110, 0, 0)");
        scrolPane.setStyle("-fx-background-color: rgba(110, 0, 0, 0)");
        main.setStyle("-fx-background-color: rgba(110, 0, 0, 0)");*/

        main.setBackground(new Background(
                new BackgroundImage(
                        new Image("https://i.pinimg.com/originals/8a/28/bf/8a28bf9807573000e4fa776efe180874.jpg"),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
                )));
       /* ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);*/

        updateView();
    }
    public void updateView(){
        int i = 0;
        int j = 0;
        for (var book: bs.getAllBooks()){
            BookView2 newBook = null;
            try {
                newBook = new BookView2(book);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            //newBook.setText(book.getName());

            bookGrid.add(newBook, j, i);
            if(++j == bookGrid.getColumnCount()){
                j = 0;
                bookGrid.addRow(i++);
            }
        }
    }

    public void UpdateCoversOnAction(ActionEvent actionEvent) {
        for (var book: bs.getAllBooks()) {
            Document doc = new Document(book.getUrl());
            int pageIndex = 1;
            Page page = doc.getPages().get_Item(pageIndex);
            FileOutputStream imageStream = null;
            String cover = "Thumbnails_" + book.getId() + ".jpg";
            try {
                imageStream = new FileOutputStream(cover);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Resolution resolution = new Resolution(300);
            JpegDevice jpegDevice = new JpegDevice(120, 150, resolution, 100);
            jpegDevice.process(page, imageStream);
            try {
                imageStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            bs.updateBookCover(book.getId(), cover);
        }
        updateView();
    }
}
