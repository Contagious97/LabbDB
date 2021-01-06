//package org.example.view;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import org.example.model.Book;
//import org.example.model.Genre;
//
//public class AddBookDialog extends Dialog<Book> {
//    private GridPane gridPane;
//
//
//    public AddBookDialog(){
//        initGrid();
//        this.getDialogPane().setContent(gridPane);
//        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
//        this.showAndWait();
//
//    }
//
//    private void initGrid(){
//        gridPane = new GridPane();
//        gridPane.setHgap(15);
//        gridPane.setVgap(15);
//        Label titleLabel = new Label("Title: ");
//        gridPane.add(titleLabel,0,0);
//        TextField titleTextInput = new TextField();
//        gridPane.add(titleTextInput,1,0);
//        Label genreLabel = new Label("Genre: ");
//        gridPane.add(genreLabel,0,1);
//        ObservableList<String> genreOptions = FXCollections.observableArrayList(
//                "DRAMA","ROMANCE", "CRIME", "HORROR", "COMEDY"
//        );
//        ComboBox genreComboBox  = new ComboBox<>(genreOptions);
//        gridPane.add(genreComboBox,1,1);
//    }
//}
