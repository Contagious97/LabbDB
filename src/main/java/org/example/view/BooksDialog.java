package org.example.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.model.Author;

import java.util.Optional;

public class BooksDialog {
    public static void closeDialog(Stage primaryStage) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("");

        dialog.setContentText("");

        dialog.initOwner(primaryStage);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.getDialogPane().setPrefSize(350, 100);

        Optional<ButtonType> s = dialog.showAndWait();
        s.ifPresent(s1 -> {
            if (s1.equals(ButtonType.APPLY)) {
                primaryStage.close();
            } else if (s1.equals(ButtonType.CLOSE)) {
                dialog.close();
            }
        });
    }
}
