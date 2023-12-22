package org.dbtest.controller;

import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.dbtest.BookView2;
import org.dbtest.Main;
import org.dbtest.ShelfButtonView;
import org.dbtest.model.Book;
import org.dbtest.service.BookService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShelfsController implements Initializable {
    public VBox shelfs;
    public ScrollPane booksPane;
    public AnchorPane editPane;
    public Button editShelf;
    public Button coversUpdate;
    public Button addShelf;
    public AnchorPane controlPane;
    public Label booksCount;
    @FXML
    private AnchorPane pane1,pane2;
    @FXML
    private Button drawerImage;
    private final BookService bs = BookService.getInstance();
    public static Shelf currentShelf;
    static public boolean editMode;
    private AtomicBoolean eFlag1 = new AtomicBoolean(true);
    private AtomicBoolean eFlag2 = new AtomicBoolean(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editMode = false;

        pane1.setVisible(false);

        AtomicBoolean flag1 = new AtomicBoolean(true);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),pane2);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnAction(event -> {
            if(flag1.get()) {
                flag1.set(false);
                pane1.setVisible(true);

                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
                fadeTransition1.setFromValue(0);
                fadeTransition1.setToValue(0.5);
                fadeTransition1.play();

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
                translateTransition1.setByX(+600);
                translateTransition1.play();

                translateTransition1.setOnFinished(actionEvent -> {
                    flag2.set(true);
                });
            }
        });

        pane1.setOnMouseClicked(event -> {
            if(flag2.get()) {
                flag2.set(false);
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
                fadeTransition1.setFromValue(0.5);
                fadeTransition1.setToValue(0);
                fadeTransition1.play();

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
                translateTransition1.setByX(-600);
                translateTransition1.play();

                translateTransition1.setOnFinished(event1 -> {
                    pane1.setVisible(false);
                    flag1.set(true);
                });
            }
        });


        editPane.setVisible(false);
        controlPane.setVisible(true);

        TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),editPane);
        translateTransition1.setByY(+50);
        translateTransition1.play();

        TranslateTransition translateTransition2=new TranslateTransition(Duration.seconds(0.5),controlPane);
        translateTransition2.setByY(0);
        translateTransition2.play();

        editShelf.setOnAction(event -> {
            if (!editPane.isVisible()) {
                startEditMode();
            } else {
                closeEditMode();
            }
        });

        for (var shelf: bs.getShelfs()){
            ShelfButtonView newShelfButton = new ShelfButtonView(shelf, booksPane);
            shelfs.getChildren().add(newShelfButton);
        }

        allBooksOnAction(new ActionEvent());
    }

    public void startEditMode(){
        if(eFlag1.get()) {
            eFlag1.set(false);
            openPanel(editPane, false, -50);
            closePanel(controlPane, false, -50);
        }
    }

    public void closeEditMode(){
        if(eFlag1.get()) {
            eFlag1.set(false);
            closePanel(editPane, false, 50);
            openPanel(controlPane, false, 50);
        }
        getCurrentShelfBooksView().forEach(x->{x.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");});
    }

    public ArrayList<BookView2> getCurrentShelfBooksView(){
        var booksPaneContent = (GridPane)booksPane.getContent();
        ArrayList<BookView2> booksView = new ArrayList<>();
        if(booksPaneContent != null) {
            var list = booksPaneContent.getChildren();
            for (var node : list)
                booksView.add((BookView2) node);
        }
        return booksView;
    }
    public void addShelfOnAction(ActionEvent event) {
        Stage stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose shelf's directory");
        File directory = dirChooser.showDialog(stage);
        ArrayList<Book> books = new ArrayList<>();
        Shelf newShelf = null;
        if (directory != null) {
            if(directory.isDirectory()){
                String shelfName = directory.getName();
                for(var doc: Objects.requireNonNull(directory.listFiles())){
                    if(doc.isFile() && FilenameUtils.getExtension(doc.getPath()).equals("pdf")){
                        //Book book = bs.saveBook(doc.getName(), doc.getAbsolutePath());//TODO absolute path
                        books.add(new Book(doc.getName(), doc.getAbsolutePath()));
                    }
                }
                newShelf = new Shelf(bs.saveShelf(shelfName, books));
            }
        }
        if(newShelf != null) {
            ShelfButtonView newShelfButton = new ShelfButtonView(newShelf.shelf, booksPane);
            shelfs.getChildren().add(newShelfButton);
        }
    }

    public void coversUpdateOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Update covers");
        alert.setContentText("Update all shelf?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData() == ButtonType.YES.getButtonData()) {
                updateCover(true);
            } else if (type.getButtonData() == ButtonType.NO.getButtonData()) {
                updateCover(false);
            }
        });
    }

    public void updateCover(boolean all){
        List<Book> books;
        if(all)
            books = bs.getAllBooks();
        else {
            if(currentShelf == null)
                return;
            books = bs.getShelfBooks(currentShelf.shelf);
        }
        for (var book: books) {
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
            updateCurrentShelf();
    }

    public void updateCurrentShelf(){
        if(currentShelf == null){
            allBooksOnAction(new ActionEvent());
        }else {
            Shelf shelfController = new Shelf(currentShelf.shelf);
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
            booksCount.setText(String.valueOf(shelfController.booksPane.getChildren().size()));
        }
    }

    public void allBooksOnAction(ActionEvent actionEvent) {
        Shelf shelf = new AllShelf();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Shelf.fxml"));
        fxmlLoader.setController(shelf);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("shelf load error");
            throw new RuntimeException(e);
        }
        booksPane.setContent(shelf.booksPane);
        currentShelf = null;//TODO
        booksCount.setText(String.valueOf(shelf.booksPane.getChildren().size()));
    }

    public void changeShelf(ActionEvent actionEvent) {
        Stage dialog = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChangeShelf.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("shelf load error");
            throw new RuntimeException(e);
        }
        ChangeShelf controller = fxmlLoader.getController();
        for (var node: shelfs.getChildren()) {
            if(!(node instanceof ShelfButtonView shelfButton))
                continue;
            JFXButton newButton = new JFXButton();
            newButton.setText(shelfButton.getText());
            newButton.setPrefSize(179, 38);
            newButton.setFont(new Font(16));
            newButton.setAlignment(Pos.BASELINE_LEFT);
            newButton.setButtonType(JFXButton.ButtonType.FLAT);
            newButton.setStyle("-fx-background-color: rgb(60,60,60,1); -fx-text-fill: white");
            newButton.setOnAction(actionEvent1 -> {
                for(var bookView: getCurrentShelfBooksView()) {
                    if(bookView.selected) {
                        bs.changeShelf(shelfButton.shelf, bookView.book);
                    }
                }
                dialog.close();
                closeEditMode();
                updateCurrentShelf();
            });
            controller.chouseShelfs.getChildren().add(newButton);
        }
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.setTitle("Change Shelf");
        dialog.setScene(new Scene(root));
        dialog.initOwner(stage);
        dialog.setMinWidth(200);
        dialog.setMinHeight(200);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        dialog.close();
        //TODO side panel
    }

    public void deleteBook(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete books");
        alert.setContentText("Are you sure delete books?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getButtonData() == ButtonType.YES.getButtonData()) {
                for(var bookView: getCurrentShelfBooksView()){
                    if(bookView.selected){
                        bs.deleteBookFromShelf(bookView.book);
                    }
                }
            } else if (type.getButtonData() == ButtonType.NO.getButtonData()) {

            }
        });

        closeEditMode();
        updateCurrentShelf();
    }

    public void openPanel(AnchorPane pane, boolean xy, double range){
        pane.setVisible(true);

        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(0.15);
        fadeTransition2.play();

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.5), pane);
        if (xy) {
            translateTransition2.setByX(range);
        } else {
            translateTransition2.setByY(range);
        }
        translateTransition2.play();
        translateTransition2.setOnFinished(actionEvent -> {
            eFlag2.set(true);
        });

        if(pane == editPane)editMode = true;
    }

    public void closePanel(AnchorPane pane, boolean xy, double range){
        FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition1.setFromValue(0.15);
        fadeTransition1.setToValue(0);
        fadeTransition1.play();

        fadeTransition1.setOnFinished(event1 -> {
            pane.setVisible(false);
            if(pane == editPane)editMode = false;
        });
        TranslateTransition translateTransition2=new TranslateTransition(Duration.seconds(0.5),pane);
        if (xy) {
            translateTransition2.setByX(range);
        } else {
            translateTransition2.setByY(range);
        }
        translateTransition2.play();
        translateTransition2.setOnFinished(actionEvent -> {
            eFlag1.set(true);
        });
    }

    public void addBook(ActionEvent actionEvent) {
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());
        FileChooser dirChooser = new FileChooser();
        dirChooser.setTitle("Open Resource File");
        File file = dirChooser.showOpenDialog(stage);
        if (file != null) {
            if(!file.isDirectory()){
                if(file.isFile() && FilenameUtils.getExtension(file.getPath()).equals("pdf")){
                    Book book = new Book(file.getName(), file.getAbsolutePath());//TODO absolute path
                    book.setShelf(currentShelf.shelf);
                    bs.saveBook(book);
                }
            }
        }
        //closePanel(editPane, false, 50);
        closeEditMode();
        updateCurrentShelf();
    }

    public void onHoverOn(MouseEvent event) {
        Button b = (Button)event.getSource();
        b.setStyle("-fx-background-color: rgba(80, 80, 80, 1); -fx-text-fill: white;");
    }

    public void onHoverOff(MouseEvent event) {
        Button b = (Button)event.getSource();
        b.setStyle("-fx-background-color: rgba(60, 60, 60, 1); -fx-text-fill: white;");
    }

    public void editShelfsOnAction(ActionEvent actionEvent) {
        Stage dialog = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EditShelf.fxml"));
        Parent root;
        EditShelf controller = new EditShelf();
        controller.shelfList = bs.getShelfs();
        fxmlLoader.setController(controller);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("shelf load error");
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.setTitle("Edit shelfs");
        dialog.setScene(new Scene(root));
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setMinWidth(400);
        dialog.setMinHeight(200);
        dialog.showAndWait();
        dialog.close();

        shelfs.getChildren().remove(1, shelfs.getChildren().size());
        for (var shelf: bs.getShelfs()){
            ShelfButtonView newShelfButton = new ShelfButtonView(shelf, booksPane);
            shelfs.getChildren().add(newShelfButton);
        }
        updateCurrentShelf();
        //TODO side panel
    }

    public void addShelfsTreeOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose shelfs root");
        File directory = dirChooser.showDialog(stage);

        ArrayList<Shelf> findedShelfs = new ArrayList<>();
        if (directory != null) {
            findedShelfs.addAll(reqGetShelfsFromDirectory(directory));
        }

        for(var newShelf: findedShelfs) {
            if (newShelf != null) {
                ShelfButtonView newShelfButton = new ShelfButtonView(newShelf.shelf, booksPane);
                shelfs.getChildren().add(newShelfButton);
            }
        }
    }

    public ArrayList<Shelf> reqGetShelfsFromDirectory(File directory){
        String shelfName = directory.getName();
        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Shelf> findedShelfs = new ArrayList<>();
        for(var doc: Objects.requireNonNull(directory.listFiles())){
            if(doc.isFile() && FilenameUtils.getExtension(doc.getPath()).equals("pdf")){
                //Book book = bs.saveBook(doc.getName(), doc.getAbsolutePath());//TODO absolute path
                books.add(new Book(doc.getName(), doc.getAbsolutePath()));
            }else if(doc.isDirectory()){
                findedShelfs.addAll(reqGetShelfsFromDirectory(doc));
            }
        }
        findedShelfs.add(new Shelf(bs.saveShelf(shelfName, books)));
        return findedShelfs;
    }

    //TODO all absolute payhs on reference path
    // do directory for Trumbnails
    // Package for windows and Linux
    // do dedicated DB instad mypostgres

}
