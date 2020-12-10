package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.fxml.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private String userName;
    private String password;
    @FXML private Label loginMessage;
    @FXML private TextField userIDField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button addBooks;
    @FXML private Button viewBooks;
    @FXML private Button issueBooks;
    @FXML private Button viewIssuedBooks;
    @FXML private Button returnBooks;
    @FXML private Button payments;
    @FXML private Button registerStudent;
    @FXML private Button studentDetails;
    @FXML private Button back;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName = "Yash";
        password = "yk";
    }
    public void login(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(userIDField.getText().equals(userName) && passwordField.getText().equals(password)){
            loginMessage.setText("Login Successful");
            stage = (Stage) loginButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        }else{
            loginMessage.setText("Invalid UserName or Password");
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

    public void mainPage(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(event.getSource().equals(addBooks)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("AddBooksPage.fxml"));

        }else if(event.getSource().equals(viewBooks)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("viewBooksPage.fxml"));

        }else if(event.getSource().equals(issueBooks)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("IssueBookPage.fxml"));

        }else if(event.getSource().equals(viewIssuedBooks)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ViewIssuedBookPage.fxml"));

        }else if(event.getSource().equals(returnBooks)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ReturnBookPage.fxml"));

        }else if(event.getSource().equals(payments)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("PaymentsPage.fxml"));

        }else if(event.getSource().equals(registerStudent)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("RegisterStudentPage.fxml"));

        }else if(event.getSource().equals(studentDetails)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("StudentDetailsPage.fxml"));

        }else if(event.getSource().equals(back)){
            stage = (Stage) addBooks.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
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

}








