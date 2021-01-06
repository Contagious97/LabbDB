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

    protected void onAddBook(Book book) throws SQLException,IOException{
        new Thread(()->{
           try {
                booksDb.addBook(book);
           } catch (Exception e){

           }
        });
    }

    protected void onGetAllBooks() throws SQLException,IOException{
        new Thread(()->{
            try {
                booksView.displayBooks(booksDb.getAllBooks());
                System.out.println("hello");
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
        });
    }

    protected void onDeleteAuthor(Author author) throws SQLException,IOException{
        new Thread(()-> {
            try {
                booksDb.deleteAuthor(author);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
