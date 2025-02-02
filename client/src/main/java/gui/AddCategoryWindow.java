package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddCategoryWindow {

    public static String display(String message) {
        Stage stage = new Stage();
        stage.setTitle("Добавление категории");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();

        Label label = new Label(message);
        TextField textFieldCategoryName = new TextField();
        textFieldCategoryName.setPromptText("Введите название категории");

        HBox hBox = new HBox();
        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(event -> {
            String categoryName = textFieldCategoryName.getText().trim();
            if (!categoryName.isEmpty()) {
                // Закрываем окно и возвращаем имя категории
                stage.setUserData(categoryName);
                stage.close();
            } else {
                // Если поле пустое, показываем ошибку
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Название категории не может быть пустым!");
                alert.showAndWait();
            }
        });

        Button cancelButton = new Button("Вернуться");
        cancelButton.setOnAction(event -> {
            stage.setUserData(null);
            stage.close();
        });

        hBox.getChildren().addAll(saveButton, cancelButton);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(label, textFieldCategoryName, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();

        return (String) stage.getUserData();
    }
}
