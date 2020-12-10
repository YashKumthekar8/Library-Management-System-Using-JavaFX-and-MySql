package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBooksPageController {
    @FXML private Button back;
    @FXML private Button search;
    @FXML private Button add;
    @FXML private Button delete;
    @FXML private TextField ISBNField;
    @FXML private TextField TitleField;
    @FXML private TextField ISBNRemove;
    @FXML private TextField TitleAdd;
    @FXML private TextField subject;
    @FXML private TextField publication;
    @FXML private TextField noOfPages;
    @FXML private TextField author;
    @FXML private TextField rack;
    @FXML private TextField edition;
    @FXML private TextField price;

    DataSourceBooks dataSourceBooks = new DataSourceBooks();

    public void Control(ActionEvent event) throws IOException {
        dataSourceBooks.open();
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
        if(event.getSource().equals(search)){
            String title = TitleField.getText();
            int ISBN = dataSourceBooks.setTitleToISBN(title);
            ISBNField.setText(String.valueOf(ISBN));
        }
        if(event.getSource().equals(add)){
            addBook();
        }else if(event.getSource().equals(delete)){
            removeBook();
        }
        dataSourceBooks.close();
    }

    public void addBook(){
        dataSourceBooks.open();
        int ISBN = dataSourceBooks.generateISBN();
        dataSourceBooks.addBooksToDataBase(ISBN,TitleAdd.getText(),subject.getText(), author.getText(),publication.getText(),Double.parseDouble(price.getText()),Integer.parseInt(noOfPages.getText()),
                Integer.parseInt(rack.getText()),Integer.parseInt(edition.getText()));
        TitleAdd.setText("");
        subject.setText("");
        author.setText("");
        publication.setText("");
        price.setText("");
        noOfPages.setText("");
        rack.setText("");
        edition.setText("");

        dataSourceBooks.close();
    }

    public void removeBook(){
        dataSourceBooks.open();
        int ISBN = Integer.parseInt(ISBNRemove.getText());
        dataSourceBooks.removeBooksFromDataBase(ISBN);
        ISBNRemove.setText("");
        dataSourceBooks.close();
    }










}




