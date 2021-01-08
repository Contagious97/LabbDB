/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.model;


import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public MockBooksDb(){
    }

    @Override
    public boolean connect(String database) throws IOException, SQLException {
        // mock implementation

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database +"?UseClientEnc=UTF8&serverTimezone=UTC", "labbguest2", "guest123");
        System.out.println("Connected...");

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
        String sql = "SELECT * FROM t_book " +
                "LEFT OUTER JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "LEFT OUTER JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE UPPER(t_book.title) LIKE '%" + searchTitle.toUpperCase()+ "%'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();



        return getBooksFromResultSet(resultSet);
    }
    @Override
    public List<Book> getBooksFromResultSet(ResultSet resultSet) throws IOException, SQLException{

        List<Book> result = new ArrayList<>();
        Book bookToAdd;
        while (resultSet.next()){
            String isbn = resultSet.getString("isbn");
            String title = resultSet.getString("title");
            Date date = resultSet.getDate("publishDate");
            int grade = resultSet.getInt("grade");
            String genre = resultSet.getString("genre");
            bookToAdd = new Book(title,isbn,date, genre, grade);
            if (!result.contains(bookToAdd)){
                result.add(bookToAdd);
            }
//            resultSet.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//            int authorID = resultSet.getInt("authorID");

            try {
//                if (authorID != 0){
                    bookToAdd.getAuthors().add(new Author(resultSet.getInt("authorID"),resultSet.getString("firstName"),resultSet.getString("lastName"),null));
//                }
            } catch (Exception e){
                System.out.println("Error adding author");
            }

        }
        resultSet.close();
        return result;
    }


    @Override
    public List<Book> searchBooksByAuthor(String searchAuthorName) throws IOException, SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE UPPER(t_author.name) LIKE '%" + searchAuthorName.toUpperCase()+ "%'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();



        return getBooksFromResultSet(resultSet);
    }

    @Override
    public List<Book> searchBooksByISBN(String searchIsbn) throws IOException, SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.isbn LIKE '%" + searchIsbn + "%'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();


        return getBooksFromResultSet(resultSet);
    }

    @Override
    public List<Book> searchBooksByRating(int searchRating) throws IOException, SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.grade =" + searchRating;
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return getBooksFromResultSet(resultSet);
    }

    @Override
    public List<Book> searchBooksByGenre(String searchGenre) throws IOException, SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.genre LIKE '%" + searchGenre + "%'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return getBooksFromResultSet(resultSet);
    }
    @Override
    public List<Book> getAllBooks() throws IOException, SQLException{
        String sql = "SELECT * FROM t_book " +
                "LEFT OUTER JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "LEFT OUTER JOIN t_author ON t_bookauthors.authorID = t_author.authorID";
        Statement statement = connection.prepareStatement(sql);
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return getBooksFromResultSet(resultSet);
    }

    @Override
    public void addBook(Book bookToAdd) throws IOException, SQLException {
        try {
            PreparedStatement addBookStatement = connection.prepareStatement(
                    "INSERT INTO t_book(isbn, title, publishDate, genre, grade) " +
                            "VALUES (" +
                            "'"+bookToAdd.getIsbn()+"'," +
                            "'"+bookToAdd.getTitle()+"'," +
                            "'"+bookToAdd.getPublishDate()+"'," +
                            "'"+bookToAdd.getGenre()+"'," +
                            "'"+bookToAdd.getGrade()+"'" +
                            ")");
            connection.setAutoCommit(false);
            addBookStatement.executeUpdate();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeBook(Book bookToRemove) throws IOException , SQLException{
        try {
             PreparedStatement removeBookStatement = connection.prepareStatement("DELETE FROM t_book WHERE t_book.isbn = '"+bookToRemove.getIsbn()+"'");
            connection.setAutoCommit(false);
            removeBookStatement.executeUpdate();
            connection.setAutoCommit(true);

        } catch (SQLException u){
            u.printStackTrace();
        }
    }

    @Override
    public void modifyBook(Book bookToModify) throws IOException, SQLException {

    }

    @Override
    public void getAllAuthors() throws IOException, SQLException {

    }

    @Override
    public void addAuthor(Author author) throws IOException, SQLException {
        try {
            PreparedStatement addAuthorStatement = connection.prepareStatement(
                    "INSERT INTO t_author(firstName,lastName,birthday) " +
                            "VALUES (" +
                            "'"+author.getFirstName()+"',"+
                            "'"+author.getLastName()+"'," +
                            "'"+author.getBirthday()+"'" +
                            ")");
            connection.setAutoCommit(false);
            addAuthorStatement.executeUpdate();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if(connection!= null){
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAuthor(Author authorToRemove) throws IOException, SQLException {
//        try {
//            PreparedStatement removeAuthorStatement = connection.prepareStatement("DELETE FROM t_book WHERE t_book.isbn = '"+authorToRemove.getAuthorID()+"'");
//            connection.setAutoCommit(false);
//            removeAuthorStatement.executeUpdate();
//            connection.setAutoCommit(true);
//
//        } catch (SQLException u){
//            u.printStackTrace();
//        }
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
