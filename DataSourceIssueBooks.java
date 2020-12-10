package sample;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSourceIssueBooks {
    String host = "jdbc:mysql://localhost:3306/librarymanagementdatabase? autoReconnect=true&useSSL=false";
    String uName = "root";
    String uPass = "Yash@0812";

    private Connection connection;
    public static DataSourceIssueBooks instance = new DataSourceIssueBooks();

    PreparedStatement selectBookItems;
    PreparedStatement selectISBN;
    PreparedStatement updateBookItem;
    PreparedStatement updateBookStatus;
    PreparedStatement insertIntoIssuedBooks;
    PreparedStatement selectIssuedBooks;
    PreparedStatement selectISBNSearch;
    PreparedStatement selectTitleToISBN;
    PreparedStatement selectBetweenDates;

    public DataSourceIssueBooks(){
        this.instance = getInstance();
    }

    public static DataSourceIssueBooks getInstance(){
        return instance;
    }

    public boolean open(){
        try{
            connection = DriverManager.getConnection(host,uName,uPass);
            selectBookItems = connection.prepareStatement("select * from bookitems");
            selectISBN = connection.prepareStatement("select * from bookitems where ISBN = ?");
            updateBookItem = connection.prepareStatement("update  bookitems set QuantityAvailable = QuantityAvailable - 1 where ISBN = ?");
            updateBookStatus = connection.prepareStatement("update  bookitems set Status = ? where ISBN = ?");
            insertIntoIssuedBooks = connection.prepareStatement("insert into issuedbooks values(?,?,?,?)");
            selectIssuedBooks = connection.prepareStatement("select * from issuedbooks");
            selectISBNSearch = connection.prepareStatement("select * from issuedbooks where ISBN = ?");
            selectTitleToISBN = connection.prepareStatement("select ISBN from books where Title = ?");
            selectBetweenDates = connection.prepareStatement("select * from issuedbooks where DateOfPurchase between ? and ?");
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to the database");
            return false;
        }
    }

    public boolean close(){
        try{
            if(selectBetweenDates != null){
                selectBetweenDates.close();
            }
            if(selectTitleToISBN != null){
                selectTitleToISBN.close();
            }
            if(selectISBNSearch != null){
                selectISBNSearch.close();
            }

            if(selectIssuedBooks != null){
                selectIssuedBooks.close();
            }
            if(updateBookStatus != null){
                updateBookStatus.close();
            }
            if(updateBookItem != null){
                updateBookItem.close();
            }
            if(selectISBN != null){
                selectISBN.close();
            }
            if(selectBookItems != null){
                selectBookItems.close();
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

    public List<BookItems> selectBookItems(){
        try{
            ResultSet resultSet = selectBookItems.executeQuery();
            List<BookItems> bookItemsList = new ArrayList<>();
            while(resultSet.next()){
                BookItems bookItems = new BookItems();
                bookItems.setISBN(resultSet.getInt(1));
                bookItems.setStatus(resultSet.getString(2));
                bookItems.setQuantityAvailable(resultSet.getInt(3));
                bookItemsList.add(bookItems);
            }
            return bookItemsList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public List<BookItems> selectBookItemsBasedOnISBN(int ISBN){
        try{
            selectISBN.setInt(1,ISBN);
            ResultSet resultSet = selectISBN.executeQuery();
            List<BookItems> bookItemsList = new ArrayList<>();
            while(resultSet.next()){
                BookItems bookItems = new BookItems();
                bookItems.setISBN(resultSet.getInt(1));
                bookItems.setStatus(resultSet.getString(2));
                bookItems.setQuantityAvailable(resultSet.getInt(3));
                bookItemsList.add(bookItems);
            }
            return bookItemsList;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public void issueBook(int libraryNo,int ISBN){
        try{
            updateBookItem.setInt(1,ISBN);
            selectISBN.setInt(1,ISBN);
            ResultSet resultSet = selectISBN.executeQuery();

            if(resultSet.next()){
                if(resultSet.getString(2).equals("Available")){
                    if(resultSet.getInt(3) > 1){
                        updateBookItem.executeUpdate();
                    }else{
                        updateBookItem.executeUpdate();
                        updateBookStatus.setString(1,"Borrowed");
                        updateBookStatus.setInt(2,ISBN);
                        updateBookStatus.executeUpdate();
                    }
                }
            }

            Date dateOfPurchase = Date.valueOf(LocalDate.now());
            Date dueDate = Date.valueOf(LocalDate.now().plusDays(30));
            insertIntoIssuedBooks.setInt(1,libraryNo);
            insertIntoIssuedBooks.setInt(2,ISBN);
            insertIntoIssuedBooks.setDate(3,dateOfPurchase);
            insertIntoIssuedBooks.setDate(4,dueDate);
            insertIntoIssuedBooks.executeUpdate();

        }catch (SQLException e){
            System.out.println("Query Failed");
        }
    }

    public List<IssuedBooks> selectIssuedBooks(){
        try{
            ResultSet resultSet = selectIssuedBooks.executeQuery();
            List<IssuedBooks> issuedBooks = new ArrayList<>();
            while(resultSet.next()){
                IssuedBooks issuedBook = new IssuedBooks();
                issuedBook.setLibraryID(resultSet.getInt(1));
                issuedBook.setISBN(resultSet.getInt(2));
                issuedBook.setDateOfPurchase(resultSet.getDate(3));
                issuedBook.setDueDate(resultSet.getDate(4));
                issuedBooks.add(issuedBook);
            }
            return issuedBooks;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public List<IssuedBooks> selectIssuedBooksBasedOnISBN(int ISBN){
        try{
            selectISBNSearch.setInt(1,ISBN);
            ResultSet resultSet = selectISBNSearch.executeQuery();
            List<IssuedBooks> issuedBooks = new ArrayList<>();
            while(resultSet.next()){
                IssuedBooks issuedBook = new IssuedBooks();
                issuedBook.setLibraryID(resultSet.getInt(1));
                issuedBook.setISBN(resultSet.getInt(2));
                issuedBook.setDateOfPurchase(resultSet.getDate(3));
                issuedBook.setDueDate(resultSet.getDate(4));
                issuedBooks.add(issuedBook);
            }
            return issuedBooks;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public List<IssuedBooks> selectIssuedBasedOnDates(Date from,Date to){
        try{
            selectBetweenDates.setDate(1,from);
            selectBetweenDates.setDate(2,to);
            ResultSet resultSet = selectBetweenDates.executeQuery();
            List<IssuedBooks> issuedBooks = new ArrayList<>();
            while(resultSet.next()){
                IssuedBooks issuedBook = new IssuedBooks();
                issuedBook.setLibraryID(resultSet.getInt(1));
                issuedBook.setISBN(resultSet.getInt(2));
                issuedBook.setDateOfPurchase(resultSet.getDate(3));
                issuedBook.setDueDate(resultSet.getDate(4));
                issuedBooks.add(issuedBook);
            }
            return issuedBooks;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return null;
        }
    }

    public int selectISBNFromTitle(String title){
        try{
            selectTitleToISBN.setString(1,title);
            ResultSet resultSet = selectTitleToISBN.executeQuery();
            resultSet.next();
            int ISBN = resultSet.getInt(1);
            return ISBN;
        }catch (SQLException e){
            System.out.println("Query Failed");
            return 0;
        }
    }


}



