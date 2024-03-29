/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.model;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import org.example.view.BooksPane;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the BooksDBInterface interface to connect and
 * use it together with the a database.
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

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database +"?UseClientEnc=UTF8&serverTimezone=UTC", "momo", "password123");
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

        Statement statement = null;
        String sql = "SELECT * FROM t_book " +
                "LEFT OUTER JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "LEFT OUTER JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE UPPER(t_book.title) LIKE '%" + searchTitle.toUpperCase()+ "%'";

        try {
            statement = connection.createStatement();

            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            return getBooksFromResultSet(resultSet);
        } finally {
            statement.close();
        }

    }
    @Override
    public List<Book> getBooksFromResultSet(ResultSet resultSet) throws IOException, SQLException{

        List<Book> result = new ArrayList<>();

        Book bookToAdd;
        boolean isNewBook = true;
        int index = 0;
        while (resultSet.next()){

            String isbn = resultSet.getString("isbn");

            for (Book book: result){
                if (book == null){
                    index = 0;
                    break;
                }
                if (book.getIsbn().equals(isbn)){
                    isNewBook = false;
                    index = result.indexOf(book);
                    break;
                }
            }

            String title = resultSet.getString("title");
            Date date = resultSet.getDate("publishDate");
            int grade = resultSet.getInt("grade");
            String genre = resultSet.getString("genre");
            if (isNewBook){
                bookToAdd = new Book(title,isbn,date, genre, grade);
                result.add(bookToAdd);
                index = result.indexOf(bookToAdd);
            }

            int authorID = resultSet.getInt("authorID");


            if (authorID != 0){
                Author author;
                author = new Author(resultSet.getInt("authorID"),resultSet.getString("firstName"),resultSet.getString("lastName"),resultSet.getDate("birthday"));
                result.get(index).addAuthorToBook(author);
            }
            isNewBook = true;
        }
        return result;
    }

    @Override
    public List<Book> searchBooksByAuthor(String searchAuthorName) throws IOException, SQLException {
        Statement statement = null;

        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE UPPER(CONCAT(t_author.firstName, ' ' ,t_author.lastName)) LIKE '%" + searchAuthorName.toUpperCase()+ "%'";

        try {
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            return getBooksFromResultSet(resultSet);
        } finally {
            statement.close();
        }
    }

    @Override
    public List<Book> searchBooksByISBN(String searchIsbn) throws IOException, SQLException {
        Statement statement = null;
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.isbn LIKE '%" + searchIsbn + "%'";
        try {
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            return getBooksFromResultSet(resultSet);
        } finally {
            statement.close();
        }
    }

    @Override
    public List<Book> searchBooksByRating(int searchRating) throws IOException, SQLException {
        Statement statement = null;
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.grade =" + searchRating;

        try {
            statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();

            return getBooksFromResultSet(resultSet);
        } finally {
            if (statement != null){
                statement.close();
            }
        }
    }

    @Override
    public List<Book> searchBooksByGenre(String searchGenre) throws IOException, SQLException {
        Statement statement = null;
        String sql = "SELECT * FROM t_book " +
                "JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "JOIN t_author ON t_bookauthors.authorID = t_author.authorID " +
                "WHERE t_book.genre LIKE '%" + searchGenre + "%'";

        try {
            statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            return getBooksFromResultSet(resultSet);
        } finally {
            if (statement!= null){
                statement.close();
            }
        }
    }

    @Override
    public List<Book> getAllBooks() throws IOException, SQLException{
        String sql = "SELECT * FROM t_book " +
                "LEFT OUTER JOIN t_bookauthors ON t_book.isbn = t_bookauthors.isbn " +
                "LEFT OUTER JOIN t_author ON t_bookauthors.authorID = t_author.authorID";
        List<Book> books = new ArrayList<>();
        try (Statement statement = connection.prepareStatement(sql)) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            books = getBooksFromResultSet(resultSet);
            return books;
        }
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
            for (Author author: bookToAdd.getAuthors()){
                if (bookToAdd.getAuthors().size() != 0){
                    addBookAuthors(bookToAdd.getIsbn(),author.getAuthorID()).executeUpdate();
                }
            }
            connection.commit();
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
            throw e;
        }
        finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void removeBook(Book bookToRemove) throws IOException , SQLException{
        try {
            PreparedStatement removeBookStatement = connection.prepareStatement("DELETE FROM t_book WHERE t_book.isbn = '"+bookToRemove.getIsbn()+"'");
            connection.setAutoCommit(false);
            removeBookStatement.executeUpdate();
            connection.commit();

        } catch (SQLException u){
            if (connection != null){
                connection.rollback();
            }
            throw u;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void modifyBook(Book bookToModify, Book newBook) throws IOException, SQLException {
        try {
            PreparedStatement modifyBookStatement = connection.prepareStatement(
                    "UPDATE t_book SET title = ?, " +
                    "publishDate = ?, " +
                    "grade = ?, " +
                    "genre = ? " +
                    "WHERE isbn = ?");

            modifyBookStatement.setString(1,newBook.getTitle());
            modifyBookStatement.setDate(2,newBook.getPublishDate());
            modifyBookStatement.setInt(3,newBook.getGrade());
            modifyBookStatement.setString(4,newBook.getGenre());
            modifyBookStatement.setString(5,newBook.getIsbn());

            PreparedStatement deleteAuthors = connection.prepareStatement("DELETE FROM t_bookauthors WHERE isbn = ?");
            deleteAuthors.setString(1,bookToModify.getIsbn());
            connection.setAutoCommit(false);
            modifyBookStatement.executeUpdate();
            deleteAuthors.executeUpdate();
            for (Author a: newBook.getAuthors()){
                if (newBook.getAuthors().size() != 0){
                    addBookAuthors(newBook.getIsbn(), a.getAuthorID()).executeUpdate();
                }
            }
            connection.commit();
        } catch (Exception e){
            if(connection!= null){
                try {
                    System.out.println("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException u){
                    System.out.println("Something went wrong when rolling back");
                }
            }
            e.printStackTrace();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public List<Author> getAllAuthors() throws IOException, SQLException {
        String sql = "SELECT * from t_author";
        Statement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();

            List<Author> authorList = new ArrayList<>();

            while (resultSet.next()){
                authorList.add(new Author(resultSet.getInt("authorID"),resultSet.getString("firstName"),
                        resultSet.getString("lastName"),resultSet.getDate("birthday")));
            }
            return authorList;
        } finally {
            statement.close();
        }
    }

    @Override
    public PreparedStatement addBookAuthors(String isbn, int authorID) throws IOException, SQLException {
        String sql = "INSERT INTO t_bookauthors(isbn, authorID) VALUES(?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,isbn);
        statement.setInt(2,authorID);

        return statement;
    }

    @Override
    public int getLatestAuthorID() throws SQLException{
        int latestAuthorID = 0;
        String sql = "SELECT authorID FROM t_author ORDER BY authorID DESC LIMIT 1";
        Statement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                latestAuthorID = rs.getInt("authorID");
            }
            return latestAuthorID;
        } finally {
            if (statement != null){
                statement.close();
            }
        }
    }

    @Override
    public void addAuthor(Author author) throws IOException, SQLException {

        String sql = "INSERT INTO t_author(firstName,lastName,birthday) " +
                "VALUES (" +
                "'"+author.getFirstName()+"',"+
                "'"+author.getLastName()+"'," +
                "'"+author.getBirthday()+"'" +
                ")";
        PreparedStatement addAuthorStatement = connection.prepareStatement(sql);
        addAuthorStatement.executeUpdate();
    }

    @Override
    public void deleteAuthor(Author authorToRemove) throws IOException, SQLException {

        Statement removeAuthorStatement = null;
        String sql = "DELETE FROM t_author WHERE t_author.authorID = '"+authorToRemove.getAuthorID()+"'";
        try {
            removeAuthorStatement = connection.createStatement();
            removeAuthorStatement.execute(sql);
        } finally {
            if (removeAuthorStatement != null){
                removeAuthorStatement.close();
            }
        }
    }

}
