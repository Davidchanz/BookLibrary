package org.dbtest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.dbtest.model.Shelf;
import org.dbtest.service.BookService;

import java.io.File;
import java.net.MalformedURLException;

public class ShelfEditer extends HBox {
    public Long Id;
    public TextField textField;
    private String name;
    public boolean deleted = false;
    private final BookService bs = BookService.getInstance();
    public ShelfEditer(Shelf shelf){
        super();
        this.Id = shelf.getId();
        this.name = shelf.getName();
        this.setSpacing(5);
        textField = new TextField(name);
        JFXCheckBox deleteCheck = new JFXCheckBox();
        deleteCheck.setCheckedColor(Color.RED);
        deleteCheck.setOnAction(actionEvent -> {
            if(deleteCheck.isSelected())
                deleted = true;
            else
                deleted = false;
        });
        Label deleteLabel = new Label("Delete");
        deleteLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(textField);
        this.getChildren().add(deleteCheck);
        this.getChildren().add(deleteLabel);
    }
}
