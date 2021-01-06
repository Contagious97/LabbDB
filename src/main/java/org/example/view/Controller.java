package org.example.view;

import javafx.application.Platform;
import org.example.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    private Book book;

    public Controller(BooksDbInterface booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
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

    protected void onAddBook(Book book){
        new Thread(()->{
           try {
               if (!isValidIsbn(book)){
                   Platform.runLater(()-> BooksPane.showAlertAndWait("Invalid ISBN. Try again",WARNING));
               } else if(!isValidTitle(book)) {
                   Platform.runLater(()-> BooksPane.showAlertAndWait("Invalid title. Try again",WARNING));
               }
               else booksDb.addBook(book);
           } catch (Exception e){
                e.printStackTrace();
           }
        }).start();
    }

    protected void onGetAllBooks(){
        new Thread(()->{
            try {
                booksView.displayBooks(booksDb.getAllBooks());
            } catch (Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }
        }).start();
    }

    protected void onAddAuthor(Author author) throws SQLException,IOException{
        new Thread(()-> {
            try {
                booksDb.addAuthor(author);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void onDeleteAuthor(Author author) throws SQLException,IOException{
        new Thread(()-> {
            try {
                booksDb.deleteAuthor(author);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean isValidTitle(Book bookToCheck){
        if(bookToCheck.getTitle().length() > 30 || book.getTitle().length() < 1){
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
