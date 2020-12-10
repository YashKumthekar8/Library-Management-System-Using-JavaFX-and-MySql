package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class IssueBookController {
    @FXML
    private Button back;
    @FXML private Button searchButtonConvert;
    @FXML private TableView<BookItems> bookItemsTable;
    @FXML private TableColumn<BookItems,Integer> ISBNCol;
    @FXML private TableColumn<BookItems,String> StatusCol;
    @FXML private TableColumn<BookItems,Integer> AvailableQuantityCol;
    @FXML private TextField titleField;
    @FXML private TextField ISBNField;
    @FXML private TextField ISBNSearchField;
    @FXML private TextField ISBNFIeldIssue;
    @FXML private TextField LibraryIDField;

    ObservableList<BookItems> bookItems;
    DataSourceIssueBooks dataSourceIssueBooks = new DataSourceIssueBooks();
    DataSourceBooks dataSourceBooks = new DataSourceBooks();

    public void initialize(){
        dataSourceIssueBooks.open();
        bookItems = FXCollections.observableArrayList(dataSourceIssueBooks.selectBookItems());
        setValuesInTable();
        dataSourceIssueBooks.close();
    }

    public void setValuesInTable(){
        try {
            ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            StatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            AvailableQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
            bookItemsTable.setItems(bookItems);
        }catch (NullPointerException ignored){

        }
    }

    public void allStatus(ActionEvent event){
        dataSourceIssueBooks.open();
        bookItems = FXCollections.observableArrayList(dataSourceIssueBooks.selectBookItems());
        setValuesInTable();
        dataSourceIssueBooks.close();
    }

    public void Control(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(event.getSource().equals(back)){
            stage = (Stage) back.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        }else if(event.getSource().equals(searchButtonConvert)){
            dataSourceBooks.open();
            int ISBN = dataSourceBooks.setTitleToISBN(titleField.getText());
            ISBNField.setText(String.valueOf(ISBN));
            dataSourceBooks.close();
        }
        if(root != null){
            Scene scene = new Scene(root);
            if(stage != null){
                stage.setScene(scene);
                stage.show();
            }
        }else{
            System.out.println("Just Add Some Scene in the Window");
        }
    }

    public void selectBookItemsISBN(ActionEvent event){
        dataSourceIssueBooks.open();
        bookItems = FXCollections.observableArrayList(dataSourceIssueBooks.selectBookItemsBasedOnISBN(Integer.parseInt(ISBNSearchField.getText())));
        dataSourceIssueBooks.close();
        setValuesInTable();
    }

    public void issueBook(ActionEvent event){
        dataSourceIssueBooks.open();
        dataSourceIssueBooks.issueBook(Integer.parseInt(LibraryIDField.getText()),Integer.parseInt(ISBNFIeldIssue.getText()));
        dataSourceIssueBooks.close();
    }
}

