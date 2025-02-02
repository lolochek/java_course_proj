package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatusSelectionWindow {

    private static String selectedStatus;

    public static String display(String message) {
        Stage stage = new Stage();
        stage.setTitle("Выбор статуса");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(windowEvent -> selectedStatus = null);

        VBox vBox = new VBox();

        Label label = new Label(message);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Создан", "В обработке", "Готов", "Отменён");
        comboBox.getSelectionModel().selectFirst();

        HBox hBox = new HBox();
        Button confirmButton = new Button("Подтвердить");
        confirmButton.setOnAction(event -> {
            selectedStatus = comboBox.getSelectionModel().getSelectedItem();
            stage.close();
        });
        Button cancelButton = new Button("Отмена");
        cancelButton.setOnAction(event -> {
            selectedStatus = null;
            stage.close();
        });
        hBox.getChildren().addAll(confirmButton, cancelButton);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(label, comboBox, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();

        return selectedStatus;
    }
}
