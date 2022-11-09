package org.dbtest.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.dbtest.model.Message;
import org.dbtest.model.MessageService;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private Label welcomeText;
    @FXML private TableView<Message> table;
    @FXML private TableColumn<Message, Long> id;
    @FXML private TableColumn<Message, String> text;
    @FXML private TextField inputText;
    @FXML private Button addMessage;
    @FXML private Button updateTable;
    private MessageService ms = MessageService.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.setEditable(true);
        id.setCellValueFactory(new PropertyValueFactory<Message, Long>("id"));
        text.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
        text.setCellFactory(TextFieldTableCell.forTableColumn());

        updateTable();
    }
    @FXML
    protected void onAddMessage(MouseEvent event){
        if(!inputText.getText().isEmpty()){
            ms.saveMessage(inputText.getText());
            updateTable();
        }
    }
    @FXML
    protected void onUpdateTable(MouseEvent event){
        updateTable();
    }
    @FXML
    protected void onUpdateMessage(TableColumn.CellEditEvent<Message, String> event){
        ms.updateMessage(event.getRowValue().getId(), event.getNewValue());
    }
    private void updateTable(){
        table.setItems(FXCollections.observableArrayList(ms.getAllMessages()));
        table.getSortOrder().add(id);
    }
    @FXML
    protected void onDeleteMessage(KeyEvent event){
        if(event.getCode() == KeyCode.DELETE){
            Message selectedItem = table.getSelectionModel().getSelectedItem();
            ms.deleteMessage(selectedItem.getId());
            updateTable();
        }
    }
}