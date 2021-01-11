package org.example.model;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 * 
 * @author anderslm@kth.se
 */
public interface BooksDbInterface {
    
    /**
     * Connects to the database.
     * @param database the database to connect to.
     * @throws SQLException if there is a SQL error
     * @return true on successful connection.
     */
    public boolean connect(String database) throws IOException, SQLException;
    /**
     * Disconnects from the database.
     * @throws SQLException if there is a SQL error
     */
    public void disconnect() throws IOException, SQLException;
    /**
     * Fetches all the book from the database including their connected authors.
     * @throws SQLException if there is a SQL error
     * @return a complete list of all the books in the database.
     */
    public List<Book> getAllBooks() throws IOException, SQLException;
    /**
     * Fetches all the authors in the database.
     * @throws SQLException if there is a SQL error
     * @return a complete list of all authors in the database.
     */
    public List<Author> getAllAuthors() throws IOException,SQLException;
    /**
     * Converts data from a resultSet to a complete list of books.
     * @param resultSet the resultSet to fetch the data from
     * @throws SQLException if there is a SQL error
     * @return a list of books created from the resultSet.
     */
    public List<Book> getBooksFromResultSet(ResultSet resultSet) throws IOException, SQLException;
    /**
     * Adds entries to the relational table between authors and book.
     * @param isbn the book's isbn.
     * @param authorID the book's author's authorID
     * @throws SQLException if there is a SQL error
     */
    public PreparedStatement addBookAuthors(String isbn, int authorID) throws IOException, SQLException;
    /**
     * This function fetches all books that have the searched title.
     * @param title the title to search for.
     * @throws SQLException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByTitle(String title) throws IOException, SQLException;
    /**
     * This function fetches all books that have the searched author.
     * @param authorName the author to search for.
     * @throws SQLException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByAuthor(String authorName) throws IOException, SQLException;
    /**
     * This function fetches all books that have the searched isbn.
     * @param isbn the isbn to search for.
     * @throws SQLException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByISBN(String isbn) throws IOException, SQLException;
    /**
     * This function fetches all books that have the searched rating.
     * @param rating the rating to search for.
     * @throws SQLException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByRating(int rating) throws IOException, SQLException;
    /**
     * This function fetches all books that have the searched genre.
     * @param genre the genre to search for.
     * @throws SQLException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByGenre(String genre) throws IOException, SQLException;
    /**
     * Is used to connect the generated authorID from the database to the book's author's authorID.
     * @throws SQLException if there is a SQL error
     * @return the latest author's authorID
     */
    public int getLatestAuthorID() throws IOException, SQLException;
    /**
     * Adds a book to the database.
     * @param book the book to add
     * @throws SQLException if there is a SQL error
     */
    public void addBook(Book book) throws IOException, SQLException;
    /**
     * Removes a book from the database.
     * @param book the book to remove
     * @throws SQLException if there is a SQL error
     */
    public void removeBook(Book book) throws IOException, SQLException;
    /**
     * Modifies a book in the database.
     * @param book the oldBook
     * @param newBook the modified book to add
     * @throws SQLException if there is a SQL error
     */
    public void modifyBook(Book book, Book newBook) throws IOException, SQLException;
    /**
     * Adds an author to the database.
     * @throws SQLException if there is a SQL error
     * @param author the author to add to the database
     */
    public void addAuthor(Author author) throws IOException,SQLException;
    /**
     * Deletes an author from the database.
     * @param author the author to delete from the database
     * @throws SQLException if there is a SQL error
     */
    public void deleteAuthor(Author author) throws IOException,SQLException;


}
