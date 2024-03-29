package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.Controller;
import org.example.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class BooksDialog extends Dialog<Book> {

    private Book bookToAdd;
    private List<Author> authorList;

    public BooksDialog(BooksDbInterface dbInterface, Controller controller) {
        this(dbInterface,controller,null);
//        final Controller controller = new Controller(dbInterface, null);
    }

    public BooksDialog(BooksDbInterface dbInterface, Controller controller,Book book) {
        super();


        authorList = new ArrayList<>();

        if(book == null){
            this.setTitle("Add Books Dialog");
            this.setHeaderText("Add book");
        }
        else{
            this.setTitle("Modify Books Dialog");
            this.setHeaderText("Modify book");
        }

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType exitButtonType = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, exitButtonType);

        Button assignAuthorButton = new Button("Assign");
        HBox hBox = new HBox();
        hBox.setSpacing(5);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Title");
        TextField isbn = new TextField();

        isbn.setPromptText("ISBN");

        if (book != null){
            title.textProperty().setValue(book.getTitle());
            isbn.textProperty().setValue(book.getIsbn());
            isbn.setDisable(true);
        }


        assignAuthorButton.setOnAction(event -> {
            AuthorsDialog dialog = new AuthorsDialog(dbInterface,controller,authorList,book);
            Optional<Author> result = dialog.showAndWait();
            result.ifPresent(author -> {
                try {
                    controller.onGetAllAuthors();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);

        grid.add(new Label("Author:"), 0, 1);
        grid.add(assignAuthorButton,1,1);

        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbn, 1, 2);


        List<Integer> ratings = new ArrayList<>();
        ratings.add(1);
        ratings.add(2);
        ratings.add(3);
        ratings.add(4);
        ratings.add(5);

        List<Genre> genres =
                new ArrayList<>(EnumSet.allOf(Genre.class));

        ComboBox genreList =  new ComboBox(FXCollections
                .observableArrayList(genres));
        if (book == null) {
            genreList.setValue("Choose a genre");
        }
        else
            genreList.setValue(book.getGenre());

        grid.add(new Label("Genres:"), 0, 3);
        grid.add(genreList, 1, 3);


        ComboBox ratingsList =
                new ComboBox(FXCollections
                        .observableArrayList(ratings));
        if (book == null){
            ratingsList.setValue("Choose a rating");
        }
        else
            ratingsList.setValue(book.getGrade());

        grid.add(new Label("Rating:"), 0, 4);
        grid.add(ratingsList, 1, 4);

        DatePicker published = new DatePicker(LocalDate.now());
        if (book != null){
            published.setValue(book.getPublishDate().toLocalDate());
        }

        grid.add(new Label("Published Date:"), 0, 5);
        grid.add(published, 1, 5);


        Node confirmButton = this.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        if (book == null){
            title.textProperty().addListener((observable, oldValue, newValue) -> {
                confirmButton.setDisable(newValue.trim().isEmpty());
            });
        }
        else confirmButton.setDisable(false);


        this.getDialogPane().setContent(grid);

        Platform.runLater(title::requestFocus);


        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                    bookToAdd = new Book(title.getText(),isbn.getText(),java.sql.Date.valueOf(published.getValue()),genreList.getValue().toString(), (Integer) ratingsList.getValue());
                    if (authorList.size() == 0 && book != null){
                        authorList = book.getAuthors();
                    }
                    bookToAdd.addAuthors(authorList);
                    return bookToAdd;
            }
            return null;
        });
    }
}
