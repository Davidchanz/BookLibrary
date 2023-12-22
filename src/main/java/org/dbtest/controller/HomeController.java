package org.dbtest.controller;

import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FilenameUtils;
import org.dbtest.Main;
import org.dbtest.model.Book;
import org.dbtest.model.Message;
import org.dbtest.service.BookService;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private VBox content;
    private Stage stage;
    private Scene scene;
    @FXML private Label welcomeText;
    @FXML private TableView<Book> table;
    @FXML private TableColumn<Book, Long> id;
    @FXML private TableColumn<Book, String> name;
    @FXML private TableColumn<Book, Void> openB;
    @FXML private TextField inputText;
    @FXML private Button addMessage;
    @FXML private Button updateTable;
    private final BookService bs = BookService.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        content.setBackground(new Background(
                new BackgroundImage(
                        new Image("https://i.pinimg.com/originals/8a/28/bf/8a28bf9807573000e4fa776efe180874.jpg"),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
                )));

        table.setEditable(true);
        id.setCellValueFactory(new PropertyValueFactory<Book, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());

        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<Book, Void>() {
                    private final Button btn = new Button("Action");{
                        btn.setOnAction((ActionEvent event) -> {
                            Book data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);

                            final Task<Void> task = new Task<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    try {
                                        Desktop.getDesktop().open(new File(data.getUrl()));
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
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        openB.setCellFactory(cellFactory);

        updateTable();
    }
    @FXML
    protected void onAddMessage(MouseEvent event){
        if(!inputText.getText().isEmpty()){
            bs.saveBook(new Book(inputText.getText()));
            updateTable();
        }
    }
    @FXML
    protected void onUpdateTable(MouseEvent event){
        updateTable();
    }
    @FXML
    protected void onUpdateMessage(TableColumn.CellEditEvent<Book, String> event){
        bs.updateBook(event.getRowValue().getId(), event.getNewValue());
    }
    private void updateTable(){
        table.setItems(FXCollections.observableArrayList(bs.getAllBooks()));
        table.getSortOrder().add(id);
    }
    @FXML
    protected void onDeleteMessage(KeyEvent event){
        if(event.getCode() == KeyCode.DELETE){
            Book selectedItem = table.getSelectionModel().getSelectedItem();
            bs.deleteBook(selectedItem);
            updateTable();
        }
        else if(event.getCode() == KeyCode.ENTER){
            System.out.println("OpenFile");
        }
    }

    public void BigPictureModeOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("BigPictureView.fxml"));
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

    public void ScanDirOnAction(ActionEvent actionEvent) {
        stage = (Stage)(((Node)actionEvent.getSource()).getScene().getWindow());
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Open Resource File");
        File file = dirChooser.showDialog(stage);
        if (file != null) {
            if(file.isDirectory()){
                for(var doc: Objects.requireNonNull(file.listFiles())){
                    if(doc.isFile() && FilenameUtils.getExtension(doc.getPath()).equals("pdf")){
                        bs.saveBook(doc.getName(), doc.getAbsolutePath());
                    }
                }
                updateTable();
            }
        }
    }
}