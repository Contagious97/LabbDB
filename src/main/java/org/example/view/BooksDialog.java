package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.example.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class BooksDialog extends Dialog<Book> {

    public BooksDialog(BooksDbInterface dbInterface) {
        this(dbInterface,new Controller(dbInterface, null),null);
//        final Controller controller = new Controller(dbInterface, null);
    }

    public BooksDialog(BooksDbInterface dbInterface, Controller controller,Book book) {
        super();

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

//        Pattern pattern = Pattern.compile("[0-9]{0,13}");
//        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
//            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
//        });
//
//        isbn.setTextFormatter(formatter);

        isbn.setPromptText("ISBN");


        assignAuthorButton.setOnAction(event -> {
            AuthorsDialog dialog = new AuthorsDialog(dbInterface);
            Optional<Author> result = dialog.showAndWait();
            result.ifPresent(author -> {
                try {
                    controller.onAddAuthor(author);
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
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
        System.out.println(genres);

        ComboBox genreList =  new ComboBox(FXCollections
                .observableArrayList(genres));
        genreList.setValue("Choose a genre");

        grid.add(new Label("Genres:"), 0, 3);
        grid.add(genreList, 1, 3);


        ComboBox ratingsList =
                new ComboBox(FXCollections
                        .observableArrayList(ratings));
        ratingsList.setValue("Choose a rating");

        grid.add(new Label("Rating:"), 0, 4);
        grid.add(ratingsList, 1, 4);

        DatePicker published = new DatePicker(LocalDate.now());

        grid.add(new Label("Published Date:"), 0, 5);
        grid.add(published, 1, 5);


        // Enable/Disable login button depending on whether a title was entered.
        Node confirmButton = this.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(grid);

// Request focus on the title field by default.
        Platform.runLater(title::requestFocus);

// Convert the result to a title-password-pair when the login button is clicked.

        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Book(title.getText(),isbn.getText(),java.sql.Date.valueOf(published.getValue()),genreList.getValue().toString(),(int)ratingsList.getValue());            }
            return null;
        });
    }
}
