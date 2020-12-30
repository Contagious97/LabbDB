package org.example.model;

import java.io.IOException;
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
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
    public boolean connect(String database) throws IOException, SQLException;
    
    public void disconnect() throws IOException, SQLException;
    
    public List<Book> searchBooksByTitle(String title) throws IOException, SQLException;

    public void searchBooksByAuthor(String authorName) throws IOException, SQLException;

    public void searchBooksByISBN(String isbn) throws IOException, SQLException;

    public void searchBooksByRating(int rating) throws IOException, SQLException;

    public void searchBooksByGenre(Genre genre) throws IOException, SQLException;

    public void addBook(Book book) throws IOException, SQLException;

    public void removeBook(Book book) throws IOException, SQLException;

    public void modifyBook(Book book, Book modifiedBook) throws IOException, SQLException;




    // TODO: Add abstract methods for all inserts, deletes and queries 
    // mentioned in the instructions for the assignement.
}
