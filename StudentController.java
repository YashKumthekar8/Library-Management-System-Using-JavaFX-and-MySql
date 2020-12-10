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
import java.sql.*;

public class StudentController {
    @FXML
    private Button back;
    @FXML
    private Button back1;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField mobileField;
    @FXML private TextArea addressArea;
    @FXML private Label libraryIDField;
    @FXML private TableView<Students> studentTable;
    @FXML private TableColumn<Students,Integer> libraryIDCol;
    @FXML private TableColumn<Students,String> nameCol;
    @FXML private TableColumn<Students,String> addressCol;
    @FXML private TableColumn<Students,String> emailNoCol;
    @FXML private TableColumn<Students,String> mobileNoCol;
    @FXML private TextField libraryIDFieldSearch;
    @FXML private TextField nameFieldSearch;


    String host = "jdbc:mysql://localhost:3306/librarymanagementdatabase? autoReconnect=true&useSSL=false";
    String uName = "root";
    String uPass = "Yash@0812";

    private Connection connection;
    private PreparedStatement insertStudents;
    private  PreparedStatement selectLibraryID;
    private PreparedStatement selectStudents;
    private PreparedStatement selectStudentsBasedOnLibraryID;
    private PreparedStatement selectStudentsBasedOnName;

    private ObservableList<Students> studentsList = FXCollections.observableArrayList();

    public void initialize(){
        open();
        selectStudents();
        close();
    }

    public void setValuesInTable() {
        try {
            libraryIDCol.setCellValueFactory(new PropertyValueFactory<>("libraryID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            emailNoCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            mobileNoCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
            studentTable.setItems(studentsList);
        } catch (NullPointerException ignored) {

        }
    }

    int generateLibraryID(){
        try{
            open();
            ResultSet resultSet = selectLibraryID.executeQuery();
            resultSet.next();
            int LibraryID = resultSet.getInt(1);
            close();
            return LibraryID + 1;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return 0;
        }
    }

    public boolean open(){
        try{
            connection = DriverManager.getConnection(host,uName,uPass);
            insertStudents = connection.prepareStatement("insert into studentaccount values(?,?,?,?,?)");
            selectLibraryID = connection.prepareStatement("select max(LibraryID) from studentaccount");
            selectStudents = connection.prepareStatement("select * from studentaccount");
            selectStudentsBasedOnLibraryID = connection.prepareStatement("select * from studentaccount where LibraryID = ?");
            selectStudentsBasedOnName = connection.prepareStatement("select * from studentaccount where Name = ?");
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to the database");
            return false;
        }
    }

    public boolean close(){
        try{
            if(selectStudents != null){
                selectStudents.close();
            }
            if(selectStudentsBasedOnLibraryID != null){
                selectStudentsBasedOnLibraryID.close();
            }
            if(selectStudentsBasedOnName != null){
                selectStudentsBasedOnName.close();
            }
            if(insertStudents != null){
                insertStudents.close();
            }
            if(selectLibraryID != null){
                selectLibraryID.close();
            }
            if(connection != null){
                connection.close();
            }
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't close the database");
            return false;
        }
    }

    public void Control(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(event.getSource().equals(back)){
            stage = (Stage) back.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        }else if(event.getSource().equals(back1)){
            stage = (Stage) back1.getScene().getWindow();
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

    public void insertStudents(ActionEvent event){
        try{
            int libraryID = generateLibraryID();

            open();
            insertStudents.setInt(1,libraryID);
            insertStudents.setString(2,nameField.getText());
            insertStudents.setString(3,addressArea.getText());
            insertStudents.setString(4,emailField.getText());
            insertStudents.setString(5,mobileField.getText());
            insertStudents.executeUpdate();
            libraryIDField.setText(String.valueOf(libraryID));
            close();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void selectStudents(){
        try{
            open();
            ResultSet resultSet = selectStudents.executeQuery();
            studentsList = FXCollections.observableArrayList();
            while(resultSet.next()){
                Students students = new Students();
                students.setLibraryID(resultSet.getInt(1));
                students.setName(resultSet.getString(2));
                students.setAddress(resultSet.getString(3));
                students.setEmail(resultSet.getString(4));
                students.setMobile(resultSet.getString(5));

               studentsList.add(students);
            }
            close();
            setValuesInTable();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void selectStudentsAll(ActionEvent event){
        selectStudents();
    }

    public void selectStudentsBasedOnLibraryID(ActionEvent event){
        try{
            open();
            selectStudentsBasedOnLibraryID.setInt(1, Integer.parseInt(libraryIDFieldSearch.getText()));
            ResultSet resultSet = selectStudentsBasedOnLibraryID.executeQuery();
            studentsList = FXCollections.observableArrayList();
            while(resultSet.next()){
                Students students = new Students();
                students.setLibraryID(resultSet.getInt(1));
                students.setName(resultSet.getString(2));
                students.setAddress(resultSet.getString(3));
                students.setEmail(resultSet.getString(4));
                students.setMobile(resultSet.getString(5));

                studentsList.add(students);
            }
            close();
            setValuesInTable();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void selectStudentsBasedOnName(ActionEvent event){
        try{
            open();
            selectStudentsBasedOnName.setString(1,nameFieldSearch.getText());
            ResultSet resultSet = selectStudentsBasedOnName.executeQuery();
            studentsList = FXCollections.observableArrayList();
            while(resultSet.next()){
                Students students = new Students();
                students.setLibraryID(resultSet.getInt(1));
                students.setName(resultSet.getString(2));
                students.setAddress(resultSet.getString(3));
                students.setEmail(resultSet.getString(4));
                students.setMobile(resultSet.getString(5));

                studentsList.add(students);
            }
            close();
            setValuesInTable();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }
}





