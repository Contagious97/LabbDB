/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.model;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 *
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se
 */
public class MockBooksDb implements BooksDbInterface {

    private Connection connection;
    private final List<Book>books;

    public MockBooksDb(){
        this.books = new ArrayList<>();
    }

    @Override
    public boolean connect(String database) throws IOException, SQLException {
        // mock implementation
        try {

            connection = DriverManager.getConnection("jdbc:mysql://myplace.se:3306/" + database +"?UseClientEnc=UTF8");
            System.out.println("Connected...");

        } catch (Exception e){
            System.out.println("Could not connect to the database");
        }
        return true;
    }

    @Override
    public void disconnect() throws IOException, SQLException {
        // mock implementation
            connection.close();
    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) 
            throws IOException, SQLException  {
        // mock implementation
        // NB! Your implementation should select the books matching
        // the search string via a query with to a database.

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_employee";
        ResultSet n = statement.executeQuery(sql);

        while (n.next()){
            int eno = n.getInt("eno");
            String name = n.getString("name");
            float salary = n.getFloat("salary");
            System.out.println(eno + "\n" + name + "\n" + salary);
        }

        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTitle)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public void searchBooksByAuthor(String searchAuthorName) throws IOException, SQLException {
        try {PreparedStatement searchAuthorStatement = connection.prepareStatement(
                "SELECT DISTINCT author FROM books WHERE name=" + searchAuthorName);
            connection.setAutoCommit(false);
            searchAuthorStatement.executeUpdate();
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void searchBooksByISBN(String searchIsbn) throws IOException, SQLException {
        try {PreparedStatement searchIsbnStatement = connection.prepareStatement(
                "SELECT * FROM books WHERE isbn=" + searchIsbn);

            connection.setAutoCommit(false);
            searchIsbnStatement.executeUpdate();
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void searchBooksByRating(int searchRating) throws IOException, SQLException {
        try {PreparedStatement searchIsbnStatement = connection.prepareStatement(
                "SELECT * FROM books WHERE isbn=" + searchRating);

            connection.setAutoCommit(false);
            searchIsbnStatement.executeUpdate();
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void searchBooksByGenre(Genre searchGenre) throws IOException, SQLException {
        try {PreparedStatement searchGenreStatement = connection.prepareStatement(
                "SELECT * FROM books WHERE genre=" + searchGenre);
            connection.setAutoCommit(false);
            searchGenreStatement.executeUpdate();
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void addBook(Book bookToAdd) throws IOException, SQLException {
        try {PreparedStatement addBookStatement = connection.prepareStatement(
                "INSERT INTO books(bookID,title,isbn,author,genre,rating)" + "VALUES(" + bookToAdd.getBookId() + "" + bookToAdd.getTitle() + "" + bookToAdd.getIsbn());
            connection.setAutoCommit(false);
            addBookStatement.executeUpdate();
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void removeBook(Book bookToRemove) throws IOException, SQLException {
        try {
            PreparedStatement addBookStatement = connection.prepareStatement(
                    "DELETE FROM books WHERE isbn =" + bookToRemove.getIsbn());

            connection.setAutoCommit(false);
            addBookStatement.executeUpdate();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u) {
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }

    @Override
    public void modifyBook(Book bookToModify, Book modifiedBook) throws IOException, SQLException {
        try {
            PreparedStatement addBookStatement = connection.prepareStatement(
                    "UPDATE books SET " + modifiedBook.getTitle()
                            + "" + modifiedBook.getIsbn()
                            + "" + modifiedBook.getGenre()
                            + "" + modifiedBook.getRating()
                            + "" + modifiedBook.getPublished()
                            + "" + modifiedBook.getStoryLine()
                            + "WHERE isbn=" + bookToModify.getIsbn() );

            connection.setAutoCommit(false);
            addBookStatement.executeUpdate();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u) {
                    System.out.println("Something went wrong when rolling back");
                }
            }
        }
    }


//    private static final Book[] DATA = {
//        new Book(1, "123456789", "Databases Illuminated", new Date(1990, 1, 1)),
//        new Book(2, "456789012", "The buried giant", new Date(2000, 1, 1)),
//        new Book(2, "567890123", "Never let me go", new Date(2000, 1, 1)),
//        new Book(2, "678901234", "The remains of the day", new Date(2000, 1, 1)),
//        new Book(2, "234567890", "Alias Grace", new Date(2000, 1, 1)),
//        new Book(3, "345678901", "The handmaids tale", new Date(2010, 1, 1))
//    };
}
