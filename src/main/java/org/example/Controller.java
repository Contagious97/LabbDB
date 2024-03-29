package org.example;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.model.*;
import org.example.view.BooksPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import static javafx.scene.control.Alert.AlertType.*;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author anderslm@kth.se
 */
public class Controller {

    private final BooksPane booksView; // view
    private final BooksDbInterface booksDb; // model

    public Controller(BooksDbInterface booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    public void onSearchSelected(String searchFor, SearchMode mode) {
        new Thread(()->{
            try {
                if (searchFor != null && searchFor.length() > 0) {
                    List<Book> result = null;
                    switch (mode) {
                        case Title:
                            result = booksDb.searchBooksByTitle(searchFor);
                            break;
                        case ISBN:
                            result = booksDb.searchBooksByISBN(searchFor);
                            break;
                        case Author:
                            result = booksDb.searchBooksByAuthor(searchFor);
                            break;
                        case Genre:
                            result = booksDb.searchBooksByGenre(searchFor);
                            break;
                        case Rating:
                            result = booksDb.searchBooksByRating(Integer.parseInt(searchFor));
                            break;
                        default:
                    }
                    if (result == null || result.isEmpty()) {
                        Platform.runLater(()-> BooksPane.showAlertAndWait(
                                "No results found.", INFORMATION));
                        booksView.clearDisplay();
                    } else {
                        booksView.displayBooks(result);
                    }
                } else {
                    Platform.runLater(()-> BooksPane.showAlertAndWait(
                            "Enter a search string!", WARNING));
                }
            } catch (Exception e) {
                Platform.runLater(()-> BooksPane.showAlertAndWait("Database error.", ERROR));
                e.printStackTrace();
            }
        }).start();

    }

    public void onAddBook(Book bookToAdd){
        new Thread(()->{
           try {
               if (!isValidIsbn(bookToAdd)){
                   Platform.runLater(()-> BooksPane.showAlertAndWait("Invalid ISBN. Try again",WARNING));
               } else if(!isValidTitle(bookToAdd)) {
                   Platform.runLater(()-> BooksPane.showAlertAndWait("Invalid title. Try again",WARNING));
               }
               else{
                    booksDb.addBook(bookToAdd);
               }

           } catch (Exception e){
               Platform.runLater(()-> BooksPane.showAlertAndWait("Error adding book",WARNING));
                e.printStackTrace();
           }
        }).start();
    }

    public void onRemoveBook(Book bookToRemove){
        new Thread(()-> {
            try {
                booksDb.removeBook(bookToRemove);
            } catch (IOException | SQLException e) {
                Platform.runLater(()-> BooksPane.showAlertAndWait("Error removing book",WARNING));
                e.printStackTrace();
            }
        }).start();
    }

    public void onModifyBook(Book bookToModify, Book newBook){
        new Thread(()->{
            try {
                booksDb.modifyBook(bookToModify, newBook);
            } catch (IOException | SQLException e){
                Platform.runLater(()-> BooksPane.showAlertAndWait("Error modifying book",WARNING));;
            }
        }).start();
    }

    public void onDisconnect() throws SQLException{
        try {
            booksDb.disconnect();
        } catch (SQLException | IOException e){
            Platform.runLater(()->BooksPane.showAlertAndWait("Error disconnecting", WARNING));
        }
    }

    public void onGetAllBooks(){
        new Thread(()->{
            try {
                booksView.displayBooks(booksDb.getAllBooks());
            } catch (Exception e){
                System.out.println("Error");
                Platform.runLater(()-> BooksPane.showAlertAndWait("There was an error getting the books. Try again",WARNING));
                e.printStackTrace();
            }
        }).start();
    }

    public void onGetAllAuthors(){
        new Thread(()->{
            try {
                booksView.displayBooks(booksDb.getAllBooks());
            } catch (Exception e){
                System.out.println("Error");
                Platform.runLater(()-> BooksPane.showAlertAndWait("There was an error getting the authors",WARNING));
                e.printStackTrace();
            }
        }).start();
    }

    public void onAddAuthor(Author authorToAdd) throws SQLException,IOException{
        new Thread(()->{
            try {
                booksDb.addAuthor(authorToAdd);
                if (booksDb.getLatestAuthorID() != 0){
                    authorToAdd.setAuthorID(booksDb.getLatestAuthorID());
                }
            } catch (IOException | SQLException e) {
                Platform.runLater(()-> BooksPane.showAlertAndWait("There was an error adding the author",WARNING));
                e.printStackTrace();
            }
        }).start();
    }

    public void onDeleteAuthor(Author authorToDelete) throws SQLException,IOException{
        new Thread(()->{
            try {
                booksDb.deleteAuthor(authorToDelete);
            } catch (IOException | SQLException e) {
                Platform.runLater(()-> BooksPane.showAlertAndWait("There was an error deleting the author",WARNING));
                e.printStackTrace();
            }
        }).start();
    }

    private boolean isValidTitle(Book bookToCheck){
        if(bookToCheck.getTitle().length() > 30 || bookToCheck.getTitle().length() < 1){
            return false;
        }
        return true;
    }

    private boolean isValidIsbn(Book bookToCheck){
        String isbn = bookToCheck.getIsbn();
        isbn = isbn.replace("-","");
        boolean matches;
        matches = isbn.matches("[0-9]{13}");
        if (!matches){
            return false;
        }
        return true;
    }
}
