/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.model;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.view.BooksPane;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 *
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se
 */
public class MockBooksDb implements BooksDbInterface {

    private MongoClient connection;
    private MongoCollection<Document> books;
    private MongoCollection<Document> authors;


    public MockBooksDb(){
    }

    @Override
    public boolean connect(String database) throws IOException, SQLException {
        // mock implementation
//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database +"?UseClientEnc=UTF8&serverTimezone=UTC", "momo", "password123");
        connection = MongoClients.create(database);
        MongoDatabase mongoDatabase = connection.getDatabase("library");
        books = mongoDatabase.getCollection("t_book");
        authors = mongoDatabase.getCollection("t_author");

        System.out.println("Connected...");

        return true;
    }

    @Override
    public void disconnect() throws IOException, SQLException {
        // mock implementation
        connection.close();
    }


    @Override
    public List<Book> getAllBooks() throws IOException, SQLException{
        ArrayList<Book> booksList = new ArrayList<>();

        for(Document book:books.find()){
            System.out.println(book.get("publishDate",""));
            LocalDate.parse(book.get("publishDate",""));


            var newBook = new Book(book.get("_id",new ObjectId()), book.get("isbn",""), book.get("genre",""),
                    book.get("grade",0), LocalDate.parse(book.get("publishDate","")), book.get("title",""));

            for(Document author:book.get("authors",new ArrayList<Document>())){
                newBook.addAuthorToBook(new Author(author.get("_id",new ObjectId()),author.get("firstName", ""), author.get("lastName", ""),LocalDate.parse(author.get("birthday",""))));
            }

            booksList.add(newBook);
        }

        return booksList;
    }

    @Override
    public List<Author> getAllAuthors() throws IOException, SQLException {
        ArrayList<Author> authorsList = new ArrayList<>();
        for (Document author : authors.find()) {
            authorsList.add(new Author(author.get("_id",new ObjectId()),author.get("firstName", ""), author.get("lastName", ""),LocalDate.parse(author.get("birthday",""))));
        }

        return authorsList;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) throws IOException, SQLException {
        List<Book> bookList = new ArrayList<>();

        var result = books.find(Filters.regex("title", Pattern.compile(title+"(?i)")));
        for(var res:result)
            bookList.add(new Book(res.get("_id",new ObjectId()), res.get("isbn",""), res.get("genre",""),
                    res.get("grade",0), LocalDate.parse(res.get("publishDate","")), res.get("title","")));

        return bookList;
    }

    @Override
    public List<Book> searchBooksByAuthor(String authorName) throws IOException, SQLException {
        List<Book> bookList = new ArrayList<>();
        var result = books.find(Filters.eq("firstName",authorName));
        for(var res:result)
            bookList.add(new Book(res.get("_id",new ObjectId()), res.get("isbn",""), res.get("genre",""),
                    res.get("grade",0), LocalDate.parse(res.get("publishDate","")), res.get("title","")));

        return bookList;
    }

    @Override
    public List<Book> searchBooksByISBN(String isbn) throws IOException, SQLException {
        List<Book> bookList = new ArrayList<>();
        var result = books.find(Filters.regex("isbn",Pattern.compile(isbn+"(?i)")));
        for(var res:result)
            bookList.add(new Book(res.get("_id",new ObjectId()), res.get("isbn",""), res.get("genre",""),
                    res.get("grade",0), LocalDate.parse(res.get("publishDate","")), res.get("title","")));

        return bookList;
    }

    @Override
    public List<Book> searchBooksByRating(int rating) throws IOException, SQLException {
        List<Book> bookList = new ArrayList<>();
        var result = books.find(Filters.eq("grade",rating));
        for(var res:result)
        bookList.add(new Book(res.get("_id",new ObjectId()), res.get("isbn",""), res.get("genre",""),
                res.get("grade",0), LocalDate.parse(res.get("publishDate","")), res.get("title","")));

        return bookList;
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) throws IOException, SQLException {
        List<Book> bookList = new ArrayList<>();
        var result = books.find(Filters.eq("genre",genre));
        for(var res:result)
            bookList.add(new Book(res.get("_id",new ObjectId()), res.get("isbn",""), res.get("genre",""),
                    res.get("grade",0), LocalDate.parse(res.get("publishDate","")), res.get("title","")));

        return bookList;
    }

    @Override
    public void addBook(Book book) throws IOException, SQLException {
        List<Document>arrayOfAuthors = new ArrayList<>();
        for(Author author:book.getAuthors()){
            arrayOfAuthors.add(new Document(Map.of("_id",author.getAuthorID(),"firstName",author.getFirstName(),"lastName",author.getLastName(),"birthday",author.getBirthday().toString())));
        }
        books.insertOne(new Document(Map.of("isbn",book.getIsbn(),"genre",book.getGenre(),"grade",book.getGrade(),"publishDate",book.getPublishDate().toString(),"title",book.getTitle(),"authors",arrayOfAuthors)));
    }

    @Override
    public void removeBook(Book book) throws IOException, SQLException {
        books.deleteOne(new Document("_id",book.getId()));
    }

    @Override
    public void modifyBook(Book book, Book newBook) throws IOException, SQLException {
        
    }

    @Override
    public void addAuthor(Author author) throws IOException, SQLException {
        var result = authors.insertOne(new Document(Map.of("firstName",author.getFirstName(),"lastName",author.getLastName(),"birthday",author.getBirthday().toString())));
        author.setAuthorID(result.getInsertedId().asObjectId().getValue());
    }

    @Override
    public void deleteAuthor(Author author) throws IOException, SQLException {
        authors.deleteOne(new Document("_id",author.getAuthorID()));
    }


}
