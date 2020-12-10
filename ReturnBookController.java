package sample;

import com.mysql.cj.xdevapi.Table;
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

public class ReturnBookController {
    @FXML
    private Button back;
    @FXML private Label libraryID;
    @FXML private Label nameOfStudent;
    @FXML private Label mobileNumber;
    @FXML private Label emailID;
    @FXML private Label address;
    @FXML private Label totalAmount;
    @FXML private TextField libraryField;
    @FXML private TextField ISBNField;
    @FXML private TableView<IssuedBooks> table;
    @FXML private TableColumn<IssuedBooks,Integer> libraryCol;
    @FXML private TableColumn<IssuedBooks,Integer> ISBNCol;
    @FXML private TableColumn<IssuedBooks, Date> dateOfPurchaseCol;
    @FXML private TableColumn<IssuedBooks,Date> dueDateCol;

    DataSourceReturnBooks dataSourceReturnBooks = new DataSourceReturnBooks();
    private ObservableList<IssuedBooks> issuedBooks = FXCollections.observableArrayList();

    public void setValuesInTable(){
        try {
            libraryCol.setCellValueFactory(new PropertyValueFactory<>("libraryID"));
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

    public void searchUsingLibraryNo(ActionEvent event){
        dataSourceReturnBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceReturnBooks.selectBasedOnLibraryID(Integer.parseInt(libraryField.getText())));
        setValuesInTable();
        Students students;
        students = dataSourceReturnBooks.selectStudents(Integer.parseInt(libraryField.getText()));
        libraryID.setText(String.valueOf(students.getLibraryID()));
        nameOfStudent.setText(students.getName());
        address.setText(students.getAddress());
        emailID.setText(students.getEmail());
        mobileNumber.setText(students.getMobile());
        totalAmount.setText(String.valueOf(DataSourceReturnBooks.amount));
        dataSourceReturnBooks.close();
    }

    public void searchUsingISBN(ActionEvent event){
        dataSourceReturnBooks.open();
        issuedBooks = FXCollections.observableArrayList(dataSourceReturnBooks.selectBasedOnISBN(Integer.parseInt(ISBNField.getText())));
        setValuesInTable();
        dataSourceReturnBooks.close();
    }

    public void returnBook(ActionEvent event){
        dataSourceReturnBooks.open();
        PaymentsController paymentsController = new PaymentsController();
        dataSourceReturnBooks.insertIntoPayments(paymentsController.generatePaymentID(), Integer.parseInt(libraryField.getText()));
        dataSourceReturnBooks.bookItem(Integer.parseInt(ISBNField.getText()));
        dataSourceReturnBooks.deleteBooks(Integer.parseInt(libraryField.getText()));
        dataSourceReturnBooks.close();
    }


}







