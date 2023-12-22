package org.dbtest.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dbtest.ShelfEditer;
import org.dbtest.model.Shelf;
import org.dbtest.service.BookService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

public class EditShelf implements Initializable {
    public VBox shelfListView;
    public List<Shelf> shelfList;
    private final BookService bs = BookService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(var shelf: shelfList){
            ShelfEditer shelfEditer = new ShelfEditer(shelf);
            shelfListView.getChildren().add(shelfEditer);
        }
    }

    public void saveShelfsOnAction(ActionEvent actionEvent) {
        for (var newShelf: shelfListView.getChildren()){
            ShelfEditer shelf = (ShelfEditer) newShelf;
            if(shelf.deleted) {
                Shelf s = new Shelf();
                s.setId(shelf.Id);
                bs.deleteShelf(s);
            }else {
                bs.updateShelf(shelf.Id, shelf.textField.getText());
            }
        }
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        stage.close();
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
