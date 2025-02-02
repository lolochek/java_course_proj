package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuantityInputWindow {

    public static int show(String message) {
        Stage stage = new Stage();
        stage.setTitle("Введите количество");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label(message);
        TextField quantityField = new TextField();
        quantityField.setPromptText("Введите количество");

        Button confirmButton = new Button("Подтвердить");
        Button cancelButton = new Button("Отмена");

        confirmButton.setOnAction(e -> {
            stage.close();
        });

        cancelButton.setOnAction(e -> {
            quantityField.setText("0");
            stage.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, quantityField, confirmButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();

        // Возвращаем введенное количество или 0, если поле пустое
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            return 0; // Если введено некорректное значение, возвращаем 0
        }
    }
}
