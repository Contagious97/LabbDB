package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.BooksDbInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AuthorsDialog extends Dialog<Author> {
    private TableView<Author> authorsTable;
    private ObservableList<Book> authorsInTable; // the data backing the table view


    public AuthorsDialog(BooksDbInterface dbInterface) {
        this(dbInterface,new Controller(dbInterface, null),null);
    }

    public AuthorsDialog(BooksDbInterface dbInterface, Controller controller, Author author) {
        super();
        this.setTitle("Author Dialog");
        this.setHeaderText("Add or delete an author");

        authorsTable = new TableView<>();
        authorsTable.setPrefWidth(300);
        authorsTable.setPrefHeight(200);
        authorsInTable =FXCollections.observableArrayList();

        TableColumn<Author, String> name= new TableColumn<>("Name");
        TableColumn<Author, String> firstName = new TableColumn<>("First name");
        TableColumn<Author, String> lastName= new TableColumn<>("Last name");
        TableColumn<Author, String> birthDay= new TableColumn<>("Birthday");

        name.getColumns().addAll(firstName, lastName);
        firstName.setPrefWidth(authorsTable.getPrefWidth()/3);
        lastName.setPrefWidth(authorsTable.getPrefWidth()/3);
        name.setPrefWidth(authorsTable.getPrefWidth()/3);
        birthDay.setPrefWidth(authorsTable.getPrefWidth()/3);
        authorsTable.setVisible(true);

        authorsTable.getColumns().addAll(name,birthDay);

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(closeButtonType);


        Button addAuthorButton = new Button("Add");
//        addAuthorButton.

         Button deleteAuthorButton = new Button("Delete");

        HBox group = new HBox();

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField authorFN = new TextField();
        authorFN.setPromptText("First Name");

        TextField authorLN = new TextField();
        authorLN.setPromptText("Last Name");

        group.getChildren().addAll(authorFN,authorLN);
        group.setPrefWidth(200);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(group, 1, 0);

//        grid.add(new Label("Last Name:"), 0, 1);
//        grid.add(authorLN, 1, 1);


        DatePicker birthday = new DatePicker(LocalDate.of(1990,1,1));
        grid.add(new Label("Birthday:"), 0, 2);
        grid.add(birthday,1,2);

        grid.add(addAuthorButton, 1, 3);

        grid.add(deleteAuthorButton, 1, 5);

        grid.add(authorsTable,1,4);


        Node confirmButton = this.getDialogPane().lookupButton(closeButtonType);
        Node addButton = this.getDialogPane().lookupButton(closeButtonType);
//        confirmButton.setDisable(true);

//        addAuthorButton.textProperty().addListener((observable, oldValue, newValue) -> {
//            confirmButton.setDisable(newValue.trim().isEmpty());
//        });
        addAuthorButton.setOnAction(event -> {
                try {
                    System.out.println("Added");
                    controller.onAddAuthor(author);
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            });

        deleteAuthorButton.setOnAction(event -> {
            try {
                System.out.println("Deleted");
                controller.onDeleteAuthor(author);
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        });
//
//        deleteAuthorButton.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("INN");
//            addButton.setDisable(newValue.trim().isEmpty());
//        });

        this.getDialogPane().setContent(grid);

// Request focus on the author field by default.
        Platform.runLater(addAuthorButton::requestFocus);

        this.setResultConverter(dialogButton -> {
            if ((dialogButton == closeButtonType) && author == null) {
                System.out.println("No author");
//                return new Pair<String, String>(author.getText(), password.getText());
            }
            else{
                System.out.println("author");
            }
            return null;
        });
    }
}
