package org.example.model;

import com.mongodb.MongoException;

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
     * @throws MongoException if there is a SQL error
     * @return true on successful connection.
     */
    public boolean connect(String database) throws IOException, MongoException;
    /**
     * Disconnects from the database.
     * @throws MongoException if there is a SQL error
     */
    public void disconnect() throws IOException, MongoException;
    /**
     * Fetches all the book from the database including their connected authors.
     * @throws MongoException if there is a SQL error
     * @return a complete list of all the books in the database.
     */
    public List<Book> getAllBooks() throws IOException, MongoException;
    /**
     * Fetches all the authors in the database.
     * @throws MongoException if there is a SQL error
     * @return a complete list of all authors in the database.
     */
    public List<Author> getAllAuthors() throws IOException,MongoException;
    /**
     * This function fetches all books that have the searched title.
     * @param title the title to search for.
     * @throws MongoException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByTitle(String title) throws IOException, MongoException;
    /**
     * This function fetches all books that have the searched author.
     * @param authorName the author to search for.
     * @throws MongoException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByAuthor(String authorName) throws IOException, MongoException;
    /**
     * This function fetches all books that have the searched isbn.
     * @param isbn the isbn to search for.
     * @throws MongoException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByISBN(String isbn) throws IOException, MongoException;
    /**
     * This function fetches all books that have the searched rating.
     * @param rating the rating to search for.
     * @throws MongoException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByRating(int rating) throws IOException, MongoException;
    /**
     * This function fetches all books that have the searched genre.
     * @param genre the genre to search for.
     * @throws MongoException if there is a SQL error
     * @return a list of books that matches the search.
     */
    public List<Book> searchBooksByGenre(String genre) throws IOException, MongoException;
    /**
     * Adds a book to the database.
     * @param book the book to add
     * @throws MongoException if there is a SQL error
     */
    public void addBook(Book book) throws IOException, MongoException;
    /**
     * Removes a book from the database.
     * @param book the book to remove
     * @throws MongoException if there is a SQL error
     */
    public void removeBook(Book book) throws IOException, MongoException;
    /**
     * Modifies a book in the database.
     * @param book the oldBook
     * @param newBook the modified book to add
     * @throws MongoException if there is a SQL error
     */
    public void modifyBook(Book book, Book newBook) throws IOException, MongoException;
    /**
     * Adds an author to the database.
     * @throws MongoException if there is a SQL error
     * @param author the author to add to the database
     */
    public void addAuthor(Author author) throws IOException,MongoException;
    /**
     * Deletes an author from the database.
     * @param author the author to delete from the database
     * @throws MongoException if there is a SQL error
     */
    public void deleteAuthor(Author author) throws IOException,MongoException;


}
