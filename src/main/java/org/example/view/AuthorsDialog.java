package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.BooksDbInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class AuthorsDialog extends Dialog<Author> {
    private final TableView<Author> authorsTable;
    private final ObservableList<Author> authorsInTable;
    private List<Author> authorList;


    public AuthorsDialog(BooksDbInterface dbInterface, Controller controller) {
        super();

        this.setTitle("Author Dialog");
        this.setHeaderText("Add or delete an author");
        authorList = new ArrayList<>();
        authorsTable = new TableView<>();
        authorsTable.setPrefWidth(300);
        authorsTable.setPrefHeight(200);
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

        this.getDialogPane().getButtonTypes().addAll(closeButtonType);
        authorsInTable = FXCollections.observableArrayList();
        authorsInTable.addAll(authorList);
        authorsTable.setItems(authorsInTable);

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


        Node confirmButton = this.getDialogPane().lookupButton(closeButtonType);
        Node addButton = this.getDialogPane().lookupButton(closeButtonType);
//        confirmButton.setDisable(true);

//        addAuthorButton.textProperty().addListener((observable, oldValue, newValue) -> {
//            confirmButton.setDisable(newValue.trim().isEmpty());
//        });
        addAuthorButton.setOnAction(event -> {
                try {
                    authorList.add(new Author(0,authorFN.getText(),authorLN.getText(),java.sql.Date.valueOf(birthday.getValue())));
                    //controller.onAddAuthor(new Author(0,authorFN.getText(),authorLN.getText(),java.sql.Date.valueOf(birthday.getValue())));
                    authorsInTable.clear();
                    for (Author author1: authorList){
                        authorsInTable.add(author1);
                    }
                } catch (Exception e ) {

                }
            });

//        Author selectedAuthor = authorsTable.getSelectionModel().getSelectedItem();
//        System.out.println(author.getAuthorID());


        deleteAuthorButton.setOnAction(event -> {
            try {

                System.out.println("Deleted");
                controller.onDeleteAuthor(null);
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
//            if ((dialogButton == closeButtonType) && author == null) {
//                System.out.println("No author");
////                return new Pair<String, String>(author.getText(), password.getText());
//            }
//            else{
                return authorsTable.getSelectionModel().selectedItemProperty().get();
//            }
        });
    }
}
