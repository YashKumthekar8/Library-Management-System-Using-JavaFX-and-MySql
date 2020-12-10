package sample;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DataSourceReturnBooks {
    String host = "jdbc:mysql://localhost:3306/librarymanagementdatabase? autoReconnect=true&useSSL=false";
    String uName = "root";
    String uPass = "Yash@0812";

    private Connection connection;
    public static DataSourceReturnBooks instance = new DataSourceReturnBooks();
    public static double amount = 0;

    public DataSourceReturnBooks(){
        this.instance = getInstance();
    }

    public static DataSourceReturnBooks getInstance(){
        return instance;
    }

    PreparedStatement searchISBN;
    PreparedStatement searchLibraryID;
    PreparedStatement selectStudent;
    PreparedStatement insertIntoPayments;
    PreparedStatement updateBookItemsQuantity;
    PreparedStatement updateBookItemsStatus;
    PreparedStatement searchISBNBookItem;
    PreparedStatement deleteIssuedBooks;

    public void open(){
        try{
            connection = DriverManager.getConnection(host,uName,uPass);
            searchISBN = connection.prepareStatement("select * from issuedbooks where ISBN = ?");
            searchLibraryID = connection.prepareStatement("select * from issuedbooks where LibraryID = ?");
            selectStudent = connection.prepareStatement("select * from studentaccount where LibraryID = ?");
            insertIntoPayments = connection.prepareStatement("insert into payment values(?,?,?,?)");
            updateBookItemsQuantity = connection.prepareStatement("update bookitems set QuantityAvailable = QuantityAvailable + 1 where ISBN = ?");
            updateBookItemsStatus = connection.prepareStatement("update bookitems set Status = 'Available' where ISBN = ?");
            searchISBNBookItem = connection.prepareStatement("select * from bookitems where ISBN = ?");
            deleteIssuedBooks = connection.prepareStatement("delete from issuedbooks where LibraryID = ?");

        }catch (SQLException e){
            System.out.println("Couldn't connect to the database");
        }
    }

    public void close(){
        try{
            if(deleteIssuedBooks != null){
                deleteIssuedBooks.close();
            }
            if(searchISBNBookItem != null) {
                searchISBNBookItem.close();
            }
            if(updateBookItemsStatus != null){
                updateBookItemsStatus.close();
            }
            if(updateBookItemsQuantity != null){
                updateBookItemsQuantity.close();
            }
            if(insertIntoPayments != null){
                insertIntoPayments.close();
            }
            if(selectStudent != null){
                selectStudent.close();
            }
            if(searchISBN != null){
                searchISBN.close();
            }
            if(searchLibraryID != null){
                searchLibraryID.close();
            }
            if(connection != null){
                connection.close();
            }
        }catch (SQLException e){
            System.out.println("Couldn't close the database");
        }
    }

    public List<IssuedBooks> selectBasedOnISBN(int ISBN){
        try{
            searchISBN.setInt(1,ISBN);
            ResultSet resultSet = searchISBN.executeQuery();
            List<IssuedBooks> issuedBooksList = new ArrayList<>();
            while(resultSet.next()){
                IssuedBooks issuedBooks = new IssuedBooks();
                issuedBooks.setLibraryID(resultSet.getInt(1));
                issuedBooks.setISBN(resultSet.getInt(2));
                issuedBooks.setDateOfPurchase(resultSet.getDate(3));
                issuedBooks.setDueDate(resultSet.getDate(4));
                issuedBooksList.add(issuedBooks);
            }
            return issuedBooksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public List<IssuedBooks> selectBasedOnLibraryID(int libraryNo){
        try{
            searchLibraryID.setInt(1,libraryNo);
            ResultSet resultSet = searchLibraryID.executeQuery();
            List<IssuedBooks> issuedBooksList = new ArrayList<>();
            IssuedBooks issuedBooks = new IssuedBooks();
            while(resultSet.next()){
                issuedBooks.setLibraryID(resultSet.getInt(1));
                issuedBooks.setISBN(resultSet.getInt(2));
                issuedBooks.setDateOfPurchase(resultSet.getDate(3));
                issuedBooks.setDueDate(resultSet.getDate(4));
                issuedBooksList.add(issuedBooks);
            }


            amount = calculate(issuedBooks.getDueDate());
            return issuedBooksList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public Students selectStudents(int libraryNo){
        try{
            selectStudent.setInt(1,libraryNo);
            ResultSet resultSet = selectStudent.executeQuery();
            Students student = new Students();
            resultSet.next();
            student.setLibraryID(resultSet.getInt(1));
            student.setName(resultSet.getString(2));
            student.setAddress(resultSet.getString(3));
            student.setEmail(resultSet.getString(4));
            student.setMobile(resultSet.getString(5));
            return student;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public double calculate(Date dueDate){
        int days = (int) ChronoUnit.DAYS.between(dueDate.toLocalDate(),LocalDate.now());
        if(days > 0){
            amount =  days * 10;
        }
        return amount;
    }

    public void insertIntoPayments(int paymentID,int libraryNo){
        try{
            insertIntoPayments.setInt(1,paymentID);
            insertIntoPayments.setInt(2,libraryNo);
            insertIntoPayments.setDouble(3,amount);
            insertIntoPayments.setDate(4,Date.valueOf(LocalDate.now()));
            insertIntoPayments.executeUpdate();

            amount = 0;

        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void bookItem(int ISBN){
        try{
            searchISBNBookItem.setInt(1,ISBN);
            ResultSet resultSet = searchISBNBookItem.executeQuery();
            BookItems bookitem = new BookItems();
            resultSet.next();
            bookitem.setISBN(resultSet.getInt(1));
            bookitem.setStatus(resultSet.getString(2));
            bookitem.setQuantityAvailable(resultSet.getInt(3));

            updateBookItemsQuantity.setInt(1,ISBN);
            updateBookItemsQuantity.executeUpdate();
            if(bookitem.getStatus().equals("Borrowed")){
                updateBookItemsStatus.setInt(1,ISBN);
                updateBookItemsStatus.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public void deleteBooks(int libraryNo){
        try{
            deleteIssuedBooks.setInt(1,libraryNo);
            deleteIssuedBooks.executeUpdate();
        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

}

