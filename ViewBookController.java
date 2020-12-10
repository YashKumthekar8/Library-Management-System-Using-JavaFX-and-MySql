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
import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookController {
    @FXML
    private Button back;
    @FXML private TableView<Books> booksTable;
    @FXML private TableColumn<Books,Integer> ISBNCol;
    @FXML private TableColumn<Books,String> TitleCol;
    @FXML private TableColumn<Books,String> SubjectCol;
    @FXML private TableColumn<Books,String> AuthorCol;
    @FXML private TableColumn<Books,String> PublicationCol;
    @FXML private TableColumn<Books,Double> PriceCol;
    @FXML private TableColumn<Books,Integer> NoOfPagesCol;
    @FXML private TableColumn<Books,Integer> RackCol;
    @FXML private TableColumn<Books,Integer> EditionCol;
    @FXML private TextField ISBNField;
    @FXML private TextField TitleField;
    @FXML private TextField SubjectField;
    @FXML private TextField AuthorField;
    @FXML private Button search;

    private DataSourceBooks dataSourceBooks = new DataSourceBooks();
    private ObservableList<Books> list;

    public void initialize(){
        dataSourceBooks.open();
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooks());
        dataSourceBooks.close();
        setValuesInTable();
    }

    public void setValuesInTable(){
        try {
            ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            SubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
            AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            PublicationCol.setCellValueFactory(new PropertyValueFactory<>("publication"));
            PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            NoOfPagesCol.setCellValueFactory(new PropertyValueFactory<>("noOfPages"));
            RackCol.setCellValueFactory(new PropertyValueFactory<>("rackNo"));
            EditionCol.setCellValueFactory(new PropertyValueFactory<>("editionNo"));
            booksTable.setItems(list);
        }catch (NullPointerException ignored){

        }
    }

    public void Control(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;
        if(event.getSource().equals(back)){
            stage = (Stage) back.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        }else if(event.getSource().equals(search)){
            if(!ISBNField.getText().equals("")){
                selectBooksISBN();
            }else if(!TitleField.getText().equals("")){
                selectBooksTitle();
            }else if(!AuthorField.getText().equals("")){
                selectBooksAuthor();
            }else if(!SubjectField.getText().equals("")){
                selectBooksSubject();
            }
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


    public void allBooks(ActionEvent event){
        dataSourceBooks.open();
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooks());
        dataSourceBooks.close();
        setValuesInTable();
    }

    public void selectBooksISBN(){
        dataSourceBooks.open();
        int ISBN = Integer.parseInt(ISBNField.getText());
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooksBasedOnISBN(ISBN));
        dataSourceBooks.close();
        setValuesInTable();
    }
    public void selectBooksTitle(){
        dataSourceBooks.open();
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooksBasedOnTitle(TitleField.getText()));
        dataSourceBooks.close();
        setValuesInTable();
    }
    public void selectBooksAuthor(){
        dataSourceBooks.open();
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooksBasedOnAuthor(AuthorField.getText()));
        dataSourceBooks.close();
        setValuesInTable();
    }
    public void selectBooksSubject(){
        dataSourceBooks.open();
        list = FXCollections.observableArrayList(dataSourceBooks.selectBooksBasedOnSubject(SubjectField.getText()));
        dataSourceBooks.close();
        setValuesInTable();
    }

}





