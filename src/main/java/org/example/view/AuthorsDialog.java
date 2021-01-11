package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.example.Controller;
import org.example.model.Author;
import org.example.model.BooksDbInterface;
import org.example.model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class AuthorsDialog extends Dialog<Author> {
    private final TableView<Author> authorsTable;
    private final ObservableList<Author> authorsInTable;
    private List<Author> authorList;
    private Author selectedAuthor;
    private List<Author> authorsToRemove;
    private ListView<Author> authorsFromBook;
    private List<Author> authorsToAddToDB;


    public AuthorsDialog(BooksDbInterface dbInterface, Controller controller, List<Author> authorsToAdd, Book book) {
        super();

        this.setTitle("Author Dialog");
        this.setHeaderText("Add or delete an author");
        authorList = new ArrayList<>();
        authorsTable = new TableView<>();
        authorsTable.setPrefWidth(300);
        authorsTable.setPrefHeight(200);
        authorsToRemove = new ArrayList<>();
        authorsToAddToDB = new ArrayList<>();
        try {
            authorList = dbInterface.getAllAuthors();
        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        TableColumn<Author, String> name= new TableColumn<>("Name");
        TableColumn<Author, String> firstName = new TableColumn<>("First name");
        TableColumn<Author, String> lastName= new TableColumn<>("Last name");
        TableColumn<Author, String> birthDay= new TableColumn<>("Birthday");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthDay.setCellValueFactory(new PropertyValueFactory<>("Birthday"));


        name.getColumns().addAll(firstName, lastName);
        firstName.setPrefWidth(authorsTable.getPrefWidth()/3);
        lastName.setPrefWidth(authorsTable.getPrefWidth()/3);
        name.setPrefWidth(authorsTable.getPrefWidth()/3);
        birthDay.setPrefWidth(authorsTable.getPrefWidth()/3);
        authorsTable.setVisible(true);
        authorsTable.getColumns().addAll(name,birthDay);

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirm = new ButtonType("Confirm",ButtonBar.ButtonData.APPLY);

        this.getDialogPane().getButtonTypes().addAll(closeButtonType, confirm);
        authorsInTable = FXCollections.observableArrayList();
        authorsInTable.addAll(authorList);
        authorsTable.setItems(authorsInTable);


        Button arrowAuthorButton = new Button("-->");
        Button deleteAuthorFromBookButton = new Button("Delete author from book");

        authorsFromBook = new ListView<>();
        System.out.println("hello helolo");
        if (book != null){
            authorsFromBook.getItems().addAll(book.getAuthors());
//            if (book.getAuthors().size() != 0){
//                for (Author a: book.getAuthors()){
//                    authorsFromBook.getItems().add(a);
//                    System.out.println(a.getAuthorID());
//                }
//            }
        }

        authorsFromBook.setMaxHeight(200);
        authorsFromBook.setPrefWidth(200);

        Button addAuthorButton = new Button("Add");
        Button deleteAuthorButton = new Button("Delete");

        HBox group = new HBox();
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField authorFN = new TextField();
        TextField authorLN = new TextField();

        authorFN.setPromptText("First Name");
        authorLN.setPromptText("Last Name");

        group.getChildren().addAll(authorFN,authorLN);
        group.setPrefWidth(200);

        Pattern pattern1 = Pattern.compile("[a-zA-Z]{0,20}");
        TextFormatter formatter1 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern1.matcher(change.getControlNewText()).matches() ? change : null;
        });

        Pattern pattern2 = Pattern.compile("[a-zA-Z]{0,20}");
        TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
        });

        authorFN.setTextFormatter(formatter1);
        authorLN.setTextFormatter(formatter2);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(group, 1, 0);

        DatePicker birthday = new DatePicker(LocalDate.of(1990,1,1));
        grid.add(new Label("Birthday:"), 0, 2);

        grid.add(birthday,1,2);
        grid.add(addAuthorButton, 1, 3);
        grid.add(deleteAuthorButton, 1, 5);
        grid.add(authorsTable,1,4);
        grid.add(arrowAuthorButton,2,4);
        grid.add(deleteAuthorFromBookButton,3,5);
        grid.add(authorsFromBook,3,4);
        grid.add(new Label("List of  all authors:"), 2, 3);

//
//        Node confirmButton = this.getDialogPane().lookupButton(closeButtonType);
//        Node addButton = this.getDialogPane().lookupButton(closeButtonType);
////        confirmButton.setDisable(true);

//
        addAuthorButton.setOnAction(event -> {
                try {
                    Author author = new Author(0,authorFN.getText(),authorLN.getText(),java.sql.Date.valueOf(birthday.getValue()));
                    authorList.add(author);
                    authorsToAddToDB.add(author);
                    authorsInTable.clear();
                    authorsInTable.addAll(authorList);
                } catch (Exception e ) {

                }
            });


        deleteAuthorButton.setOnAction(event -> {
            try {
                selectedAuthor = authorsTable.getSelectionModel().getSelectedItem();
                authorList.remove(selectedAuthor);
                authorsToRemove.add(selectedAuthor);
                authorsInTable.clear();
                authorsInTable.addAll(authorList);
            } catch (Exception e){
                e.printStackTrace();
            }


        });

        deleteAuthorFromBookButton.setOnAction(event ->{
            try {
                if(authorsFromBook.getSelectionModel().getSelectedItem() != null){
                    authorsFromBook.getItems().remove(authorsFromBook.getSelectionModel().getSelectedItem());
                }
                else BooksPane.showAlertAndWait("No Author Chosen", Alert.AlertType.WARNING);

            } catch (Exception e){
                e.printStackTrace();
            }
        });

        arrowAuthorButton.setOnAction(event -> {
            System.out.println("arrow");
            selectedAuthor = authorsTable.getSelectionModel().selectedItemProperty().get();
            authorsFromBook.getItems().add(selectedAuthor);
        });

        this.getDialogPane().setContent(grid);

// Request focus on the author field by default.
        Platform.runLater(addAuthorButton::requestFocus);

        this.setResultConverter(dialogButton -> {
//            if ((dialogButton == closeButtonType) && author == null) {
//                System.out.println("No author");
////                return new Pair<String, String>(author.getText(), password.getText());
//            }
//            else{
            if (dialogButton == confirm){
                try {
                    for (Author a: authorsToRemove){
                        if (a != null){
                            controller.onDeleteAuthor(a);
                        }
                    }

                    for (Author a: authorsToAddToDB){
                        if (a != null){
                            controller.onAddAuthor(a);
                        }
                    }
                    authorsToAdd.clear();
                    authorsToAdd.addAll(authorsFromBook.getItems());
                    System.out.println("Authors from book: " + authorsFromBook.getItems().toString() + "authorID: " + authorsFromBook.getItems().get(0).getAuthorID());
                } catch (Exception e){
                    e.printStackTrace();
                }
                return authorsTable.getSelectionModel().selectedItemProperty().get();
            }
            return null;

//            }
        });
    }
}
