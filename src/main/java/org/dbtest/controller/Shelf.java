package org.dbtest.controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.dbtest.BookView2;
import org.dbtest.model.Book;
import org.dbtest.service.BookService;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

public class Shelf implements Initializable {
    protected final BookService bs = BookService.getInstance();
    @FXML
    public GridPane booksPane;
    public org.dbtest.model.Shelf shelf;
    public Shelf(){
    }

    public Shelf(org.dbtest.model.Shelf shelf){
        this();
        this.shelf = shelf;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //booksPane.setOnMouseClicked(this::clickGrid);
        updateView();
    }

    public void updateView(){
        int i = 0;
        int j = 0;
        for (var book: bs.getShelfBooks(shelf)){
            BookView2 newBook = null;
            try {
                newBook = new BookView2(book);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            booksPane.add(newBook, j, i);
            if(++j == booksPane.getColumnCount()){
                j = 0;
                booksPane.addRow(i++);
            }
        }
    }

    /*public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != booksPane) {
            // click on descendant node
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
        }
    }*/
}
