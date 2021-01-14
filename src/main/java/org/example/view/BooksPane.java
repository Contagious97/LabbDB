package org.example.view;

import com.mongodb.MongoException;
import javafx.stage.Stage;
import org.example.Controller;
import org.example.model.BooksDbInterface;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.model.SearchMode;
import org.example.model.Book;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author anderslm@kth.se
 */
public class BooksPane extends VBox {

    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view
    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;
    private BooksDbInterface dbInterface;
    private Book selectedBook;
    private final Stage primaryStage;

    private MenuBar menuBar;

    public BooksPane(Stage primaryStage, BooksDbInterface booksDb) throws IOException, SQLException {
        final Controller controller = new Controller(booksDb, this);
        this.primaryStage = primaryStage;
        this.init(controller);
        this.dbInterface = booksDb;
        //displayBooks(booksTable.getItems());

    }

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    public void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }

    public void clearDisplay(){
        booksInTable.clear();
    }
    
    /**
     * Notify user on input error or exceptions.
     * 
     * @param msg the message
     * @param type types: INFORMATION, WARNING et c.
     */
    public static void showAlertAndWait(String msg, Alert.AlertType type) {
        // types: INFORMATION, WARNING et c.
        Alert alert = new Alert(type, msg);
        alert.showAndWait();

    }

    private void init(Controller controller) {

        booksInTable = FXCollections.observableArrayList();
        // init views and event handlers
        initBooksTable();
        initSearchView(controller);
        initMenus(controller);

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)

        // define columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, String> ratingCol = new TableColumn<>("Rating");
        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("PublishDate");
        booksTable.getColumns().addAll(titleCol, isbnCol, ratingCol, genreCol, publishedCol);
        // give title column some extra space
        titleCol.prefWidthProperty().bind(booksTable.widthProperty().divide(5));
        isbnCol.prefWidthProperty().bind(booksTable.widthProperty().divide(5));
        ratingCol.prefWidthProperty().bind(booksTable.widthProperty().divide(5));
        genreCol.prefWidthProperty().bind(booksTable.widthProperty().divide(5));
        publishedCol.prefWidthProperty().bind(booksTable.widthProperty().divide(5));

        // define how to fill data for each cell, 
        // get values from Book properties
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // associate the table view with the data
        booksTable.setItems(booksInTable);
    }

    private void initSearchView(Controller controller) {
        searchField = new TextField();
        searchField.setPromptText("Search for...");
        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton = new Button("Search");

        // event handling (dispatch to controller)
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchFor = searchField.getText();
                SearchMode mode = searchModeBox.getValue();
                controller.onSearchSelected(searchFor, mode);
            }
        });
    }

    private void initMenus(Controller controller) {

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");

        exitItem.setOnAction(actionEvent -> {
            try {
                controller.onDisconnect();
                primaryStage.close();
            } catch (MongoException e){
            }
        });

        fileMenu.getItems().addAll(exitItem);

        Menu manageMenu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add Book");
        MenuItem modifyItem = new MenuItem("Modify Book");

        modifyItem.setOnAction(t->{
            if (booksTable.getSelectionModel().getSelectedItem() == null){
                showAlertAndWait("No book selected", Alert.AlertType.WARNING);
            }
            else {
                Book bookToModify = booksTable.getSelectionModel().getSelectedItem();
                int index = booksInTable.indexOf(bookToModify);
                BooksDialog dialog = new BooksDialog(dbInterface, controller, bookToModify);
                Optional<Book> result = dialog.showAndWait();
                result.ifPresent(book -> {
                    try {
                        controller.onModifyBook(bookToModify,book);
                        booksInTable.remove(index);
                        booksInTable.add(index,book);
                        System.out.println(book.getAuthors().toString());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        });

        addItem.setOnAction(t -> {
            BooksDialog dialog = new BooksDialog(dbInterface,controller);
            Optional<Book> result = dialog.showAndWait();
            result.ifPresent(controller::onAddBook);
            controller.onGetAllBooks();
        });

        MenuItem removeItem = new MenuItem("Remove");

        booksTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            selectedBook = newValue;
        }));

        removeItem.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this book?");
                    Optional<ButtonType> buttonChoice = alert.showAndWait();
                    if(buttonChoice.get() == ButtonType.OK){
                        booksInTable.remove(selectedBook);
                    }
            controller.onRemoveBook(selectedBook);
        });


        MenuItem updateItem = new MenuItem("Update");

        updateItem.setOnAction(event -> {
            try {
                controller.onGetAllBooks();
            } catch (Exception e){

            }
        });

        manageMenu.getItems().addAll(addItem, modifyItem, removeItem, updateItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, manageMenu);
    }
}
