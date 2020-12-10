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
import java.sql.*;

public class PaymentsController {
    @FXML
    private Button back;

    String host = "jdbc:mysql://localhost:3306/librarymanagementdatabase? autoReconnect=true&useSSL=false";
    String uName = "root";
    String uPass = "Yash@0812";

    private Connection connection;

    PreparedStatement selectPayments;
    PreparedStatement selectPaymentsBasedOnLibraryID;
    PreparedStatement selectPaymentID;

    @FXML private TableView<Payments> paymentTable;
    @FXML private TableColumn<Payments,Integer> paymentIDCol;
    @FXML private TableColumn<Payments,Integer> libraryIDCol;
    @FXML private TableColumn<Payments,Double> amountCol;
    @FXML private TableColumn<Payments, Date> dateCol;

    @FXML private TextField libraryIDField;

    private ObservableList<Payments> paymentsList = FXCollections.observableArrayList();

    public void initialize(){
        open();
        selectPayments();
        setValuesInTable();
        close();
    }

    int generatePaymentID(){
       try{
           open();
           ResultSet resultSet = selectPaymentID.executeQuery();
           resultSet.next();
           int OrderID = resultSet.getInt(1);
           close();
           return OrderID + 1;
       }catch (SQLException e){
           System.out.println("Query Failed");
           return 0;
       }
    }

    public void setValuesInTable(){
        try {
            paymentIDCol.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
            libraryIDCol.setCellValueFactory(new PropertyValueFactory<>("libraryID"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            paymentTable.setItems(paymentsList);
        }catch (NullPointerException ignored){

        }
    }

    public boolean open(){
        try{
            connection = DriverManager.getConnection(host,uName,uPass);
            selectPayments = connection.prepareStatement("select * from payment");
            selectPaymentsBasedOnLibraryID = connection.prepareStatement("select * from payment where LibraryID = ?");
            selectPaymentID = connection.prepareStatement("select max(PaymentID) from payment");
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to the database");
            return false;
        }
    }

    public boolean close(){
        try{
            if(selectPaymentID != null){
                selectPaymentID.close();
            }
            if(selectPayments != null){
                selectPayments.close();
            }
            if(selectPaymentsBasedOnLibraryID != null){
                selectPaymentsBasedOnLibraryID.close();
            }
            if(connection != null){
                connection.close();
            }
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't close the database connection");
            return false;
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

    public void selectPayments(){
        try{
            open();
            ResultSet resultSet = selectPayments.executeQuery();
            paymentsList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Payments payments = new Payments();
                payments.setPaymentID(resultSet.getInt(1));
                payments.setLibraryID(resultSet.getInt(2));
                payments.setAmount(resultSet.getDouble(3));
                payments.setDate(resultSet.getDate(4));

                paymentsList.add(payments);
            }
            close();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void selectAllPayments(ActionEvent event) {
        selectPayments();
        setValuesInTable();
    }

    public void searchBasedOnLibraryID(ActionEvent event){
        try{
            open();
            selectPaymentsBasedOnLibraryID.setInt(1,Integer.parseInt(libraryIDField.getText()));
            System.out.println(Integer.parseInt(libraryIDField.getText()));
            ResultSet resultSet = selectPaymentsBasedOnLibraryID.executeQuery();
            paymentsList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Payments payments = new Payments();
                payments.setPaymentID(resultSet.getInt(1));
                payments.setLibraryID(resultSet.getInt(2));
                payments.setAmount(resultSet.getDouble(3));
                payments.setDate(resultSet.getDate(4));

                paymentsList.add(payments);
            }
            for(Payments item : paymentsList){
                item.getPaymentID();
            }
            close();
            setValuesInTable();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }

    }
}






