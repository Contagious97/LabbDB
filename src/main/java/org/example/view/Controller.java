package org.example.view;

import javafx.application.Platform;
import org.example.model.Genre;
import org.example.model.SearchMode;
import org.example.model.Book;
import org.example.model.BooksDbInterface;
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

    protected void onAddBook(){
        new Thread(()->{
           try {

           } catch (Exception e){

           }
        });
    }

    protected void onGetAllBooks(){
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
}
