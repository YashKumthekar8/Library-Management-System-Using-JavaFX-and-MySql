package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceBooks {
    String host = "jdbc:mysql://localhost:3306/librarymanagementdatabase? autoReconnect=true&useSSL=false";
    String uName = "root";
    String uPass = "Yash@0812";

    private Connection connection;
    public static DataSourceBooks instance = new DataSourceBooks();
    PreparedStatement addBooks;
    PreparedStatement removeBooks;
    PreparedStatement TitleToISBN;
    PreparedStatement selectISBN;
    PreparedStatement selectBooks;
    PreparedStatement selectBooksBasedOnISBN;
    PreparedStatement selectBooksBasedOnSubject;
    PreparedStatement selectBooksBasedOnTitle;
    PreparedStatement selectBooksBasedOnAuthor;

    public DataSourceBooks(){
        this.instance = getInstance();
    }

    public static DataSourceBooks getInstance(){
        return instance;
    }

    public boolean open(){
        try{
            connection = DriverManager.getConnection(host,uName,uPass);
            addBooks = connection.prepareStatement("insert into books values(?,?,?,?,?,?,?,?,?)");
            removeBooks = connection.prepareStatement("delete from books where ISBN = ?");
            TitleToISBN = connection.prepareStatement("select ISBN from books where Title = ?");
            selectISBN = connection.prepareStatement("select max(ISBN) from books");
            selectBooks = connection.prepareStatement("select * from books");
            selectBooksBasedOnISBN = connection.prepareStatement("select * from books where ISBN = ?");
            selectBooksBasedOnTitle = connection.prepareStatement("select * from books where Title = ?");
            selectBooksBasedOnAuthor = connection.prepareStatement("select * from books where Author = ?");
            selectBooksBasedOnSubject = connection.prepareStatement("select * from books where Subject = ?");
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to the database");
            return false;
        }
    }

    public boolean close(){
        try{
            if(selectBooks != null){
                selectBooks.close();
            }
            if(selectBooksBasedOnISBN !=  null){
                selectBooksBasedOnISBN.close();
            }
            if(selectBooksBasedOnTitle !=  null){
                selectBooksBasedOnTitle.close();
            }
            if(selectBooksBasedOnAuthor !=  null){
                selectBooksBasedOnAuthor.close();
            }
            if(selectBooksBasedOnSubject !=  null){
                selectBooksBasedOnSubject.close();
            }
            if(TitleToISBN != null){
                TitleToISBN.close();
            }
            if(removeBooks != null){
                removeBooks.close();
            }
            if(addBooks != null){
                addBooks.close();
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

    public int generateISBN(){
        try{
            ResultSet resultSet = selectISBN.executeQuery();
            resultSet.next();
            int ISBN = resultSet.getInt(1);
            return ISBN + 1;
        }catch (SQLException e){
            System.out.println(e);
            return 0;
        }
    }

    public void addBooksToDataBase(int ISBN,String Title,String Subject,String Author,String publication,double price,int noOfPages,int rackNo,int editionNo){
        try{
            addBooks.setInt(1,ISBN);
            addBooks.setString(2,Title);
            addBooks.setString(3,Subject);
            addBooks.setString(4,Author);
            addBooks.setString(5,publication);
            addBooks.setDouble(6,price);
            addBooks.setInt(7,noOfPages);
            addBooks.setInt(8,rackNo);
            addBooks.setInt(9,editionNo);

            addBooks.executeUpdate();
        }catch (SQLException e){
            System.out.println("Couldn't add the item to the database");
        }
    }
    public void removeBooksFromDataBase(int ISBN){
        try{
            removeBooks.setInt(1,ISBN);
            removeBooks.executeUpdate();
        }catch (SQLException e){
            System.out.println("Couldn't remove the item from the database");
        }
    }

    public int setTitleToISBN(String Title){
        try{
            TitleToISBN.setString(1,Title);
            int ISBN = 0;
            ResultSet resultSet = TitleToISBN.executeQuery();
            while(resultSet.next()){
                ISBN = resultSet.getInt(1);
            }
            return ISBN;
        }catch (SQLException e){
            System.out.println("Couldn't select the item from the database");
            return 0;
        }
    }

    public List<Books> selectBooks(){
        try{
            ResultSet resultSet = selectBooks.executeQuery();
            List<Books> booksList = new ArrayList<>();
            while (resultSet.next()){
                Books books = new Books();
                books.setISBN(resultSet.getInt(1));
                books.setTitle(resultSet.getString(2));
                books.setSubject(resultSet.getString(3));
                books.setAuthor(resultSet.getString(4));
                books.setPublication(resultSet.getString(5));
                books.setPrice(resultSet.getDouble(6));
                books.setNoOfPages(resultSet.getInt(7));
                books.setRackNo(resultSet.getInt(8));
                books.setEditionNo(resultSet.getInt(9));
                booksList.add(books);
            }
            return booksList;
        } catch (SQLException e) {
            System.out.println("Couldn't select books from the database");
            return null;
        }
    }

    public List<Books> selectBooksBasedOnISBN(int ISBN){
        try{
            selectBooksBasedOnISBN.setInt(1,ISBN);
            ResultSet resultSet = selectBooksBasedOnISBN.executeQuery();
            List<Books> booksList = new ArrayList<>();
            while (resultSet.next()){
                Books books = new Books();
                books.setISBN(resultSet.getInt(1));
                books.setTitle(resultSet.getString(2));
                books.setSubject(resultSet.getString(3));
                books.setAuthor(resultSet.getString(4));
                books.setPublication(resultSet.getString(5));
                books.setPrice(resultSet.getDouble(6));
                books.setNoOfPages(resultSet.getInt(7));
                books.setRackNo(resultSet.getInt(8));
                books.setEditionNo(resultSet.getInt(9));
                booksList.add(books);
            }
            return booksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public List<Books> selectBooksBasedOnTitle(String Title){
        try{
            selectBooksBasedOnTitle.setString(1,Title);
            ResultSet resultSet = selectBooksBasedOnTitle.executeQuery();
            List<Books> booksList = new ArrayList<>();
            while (resultSet.next()){
                Books books = new Books();
                books.setISBN(resultSet.getInt(1));
                books.setTitle(resultSet.getString(2));
                books.setSubject(resultSet.getString(3));
                books.setAuthor(resultSet.getString(4));
                books.setPublication(resultSet.getString(5));
                books.setPrice(resultSet.getDouble(6));
                books.setNoOfPages(resultSet.getInt(7));
                books.setRackNo(resultSet.getInt(8));
                books.setEditionNo(resultSet.getInt(9));
                booksList.add(books);
            }
            return booksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }
    public List<Books> selectBooksBasedOnAuthor(String Author){
        try{
            selectBooksBasedOnAuthor.setString(1,Author);
            ResultSet resultSet = selectBooksBasedOnAuthor.executeQuery();
            List<Books> booksList = new ArrayList<>();
            while (resultSet.next()){
                Books books = new Books();
                books.setISBN(resultSet.getInt(1));
                books.setTitle(resultSet.getString(2));
                books.setSubject(resultSet.getString(3));
                books.setAuthor(resultSet.getString(4));
                books.setPublication(resultSet.getString(5));
                books.setPrice(resultSet.getDouble(6));
                books.setNoOfPages(resultSet.getInt(7));
                books.setRackNo(resultSet.getInt(8));
                books.setEditionNo(resultSet.getInt(9));
                booksList.add(books);
            }
            return booksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }
    public List<Books> selectBooksBasedOnSubject(String Subject){
        try{
            selectBooksBasedOnSubject.setString(1,Subject);
            ResultSet resultSet = selectBooksBasedOnSubject.executeQuery();
            List<Books> booksList = new ArrayList<>();
            while (resultSet.next()){
                Books books = new Books();
                books.setISBN(resultSet.getInt(1));
                books.setTitle(resultSet.getString(2));
                books.setSubject(resultSet.getString(3));
                books.setAuthor(resultSet.getString(4));
                books.setPublication(resultSet.getString(5));
                books.setPrice(resultSet.getDouble(6));
                books.setNoOfPages(resultSet.getInt(7));
                books.setRackNo(resultSet.getInt(8));
                books.setEditionNo(resultSet.getInt(9));
                booksList.add(books);
            }
            return booksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }
}



