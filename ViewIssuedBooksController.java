package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class ViewIssuedBooksController {
    @FXML
    private Button back;
    @FXML private TableView<IssuedBooks> table;
    @FXML private TableColumn<IssuedBooks,Integer> libraryIDCol;
    @FXML private TableColumn<IssuedBooks,Integer> ISBNCol;
    @FXML private TableColumn<IssuedBooks, Date> dateOfPurchaseCol;
    @FXML private TableColumn<IssuedBooks,Date> dueDateCol;
    @FXML private TextField ISBNField1;
    @FXML private TextField ISBNField2;
    @FXML private TextField titleField;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    DataSourceIssueBooks dataSourceIssueBooks = new DataSourceIssueBooks();
    private ObservableList<IssuedBooks> issuedBooks = FXCollections.observableArrayList();

    public void initialize(){
        dataSourceIssueBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceIssueBooks.selectIssuedBooks());
        dataSourceIssueBooks.close();
        setValuesInTable();
    }

    public void allRecords(ActionEvent event){
        dataSourceIssueBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceIssueBooks.selectIssuedBooks());
        dataSourceIssueBooks.close();
        setValuesInTable();
    }

    public void setValuesInTable(){
        try {
            libraryIDCol.setCellValueFactory(new PropertyValueFactory<>("libraryID"));
            ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            dateOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("dateOfPurchase"));
            dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            table.setItems(issuedBooks);
        }catch (NullPointerException ignored){

        }
    }


    public void Control(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(event.getSource().equals(back)){
            stage = (Stage) back.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
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

    public void selectBasedOnISBN(ActionEvent event){
        dataSourceIssueBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceIssueBooks.selectIssuedBooksBasedOnISBN(Integer.parseInt(ISBNField1.getText())));
        dataSourceIssueBooks.close();
        setValuesInTable();
    }

    public void selectBasedOnDates(ActionEvent event){
        dataSourceIssueBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceIssueBooks.selectIssuedBasedOnDates(Date.valueOf(dateFrom.getValue()),Date.valueOf(dateTo.getValue())));
        dataSourceIssueBooks.close();
        setValuesInTable();
    }

    public void selectISBNFromTitle(ActionEvent event){
        dataSourceIssueBooks.open();
        int ISBN = dataSourceIssueBooks.selectISBNFromTitle(titleField.getText());
        dataSourceIssueBooks.close();
        ISBNField2.setText(String.valueOf(ISBN));
    }

}
