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

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database +"?UseClientEnc=UTF8&serverTimezone=UTC", "labbguest", "guest123");
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
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
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
            System.out.println(isbn + "\n" + title + "\n" + date);
            bookToAdd = new Book(title,isbn,date, genre, grade);
            if (!result.contains(bookToAdd)){
                result.add(bookToAdd);
            }

            int authorID = resultSet.getInt("authorID");
            try {
                if (authorID != 0){
                    bookToAdd.getAuthors().add(new Author(resultSet.getString("name"),authorID));
                }
            } catch (Exception e){
                System.out.println("Error adding author");
            }

        }
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
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM t_book";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return getBooksFromResultSet(resultSet);
    }


    @Override
    public void addBook(Book bookToAdd) throws IOException, SQLException {
        try {
            PreparedStatement addBookStatement = connection.prepareStatement(
                "UPDATE books INSERT INTO t_book(isbn, title, publishDate, genre, grade)" +
                        "VALUES(" + "'" + bookToAdd.getIsbn()+ "'," +
                        "'" + bookToAdd.getTitle()+ "'," +
                        "'" + bookToAdd.getPublishDate()+ "'," +
                        "'" + bookToAdd.getGenre()+ "'," +
                        "'" + bookToAdd.getGrade()+ "'");
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
            PreparedStatement removeBookStatement = connection.prepareStatement(
              "UPDATE books DELETE FROM t_book WHERE t_book.isbn =" + bookToRemove.getIsbn()
            );

        } catch (SQLException e){
            System.out.println("Error");
        }
    }

    @Override
    public void modifyBook(Book bookToModify) throws IOException, SQLException {

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
