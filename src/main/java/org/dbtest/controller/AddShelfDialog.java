package org.dbtest.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.dbtest.model.Book;
import org.dbtest.model.Shelf;
import org.dbtest.service.BookService;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class AddShelfDialog {
    @FXML public TextField shelfName;
    public ListView bookList;
    private File directory;
    private final BookService bs = BookService.getInstance();
    public Shelf shelf;

    public void chouseDirectoryOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Open Resource File");
        directory = dirChooser.showDialog(stage);
        ArrayList<Book> books = new ArrayList<>();
        if (directory != null) {
            if(directory.isDirectory()){
                for(var doc: Objects.requireNonNull(directory.listFiles())){
                    if(doc.isFile() && FilenameUtils.getExtension(doc.getPath()).equals("pdf")){
                        //Book book = bs.saveBook(doc.getName(), doc.getAbsolutePath());//TODO absolute path
                        books.add(new Book(doc.getName(), doc.getAbsolutePath()));
                    }
                }
                shelf = bs.saveShelf(shelfName.getText(), books);
            }
        }
        stage.close();
    }
}
