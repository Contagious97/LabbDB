package org.example;

import org.example.model.BooksDbInterface;
import org.example.model.MockBooksDb;
import org.example.view.BooksPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Application start up.
 *
 * @author anderslm@kth.se
 */
public class BooksDbClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {

        MockBooksDb booksDb = new MockBooksDb(); // model
        // Don't forget to connect to the db, somewhere...

        BooksPane root = new BooksPane(booksDb);

        try {
            booksDb.connect("sys");
            root.displayBooks(booksDb.getAllBooks());
            System.out.println("Connected to database");
        } catch (Exception e){
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {}
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
