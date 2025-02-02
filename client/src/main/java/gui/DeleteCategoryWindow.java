package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import request.tdo.CategoryDTO;

import java.util.List;

public class DeleteCategoryWindow {

    public static CategoryDTO display(String message, List<CategoryDTO> categories) {
        Stage stage = new Stage();
        stage.setTitle("Удаление категории");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();

        Label label = new Label(message);
        ComboBox<CategoryDTO> comboBoxCategories = new ComboBox<>();
        comboBoxCategories.getItems().addAll(categories);
        comboBoxCategories.setPromptText("Выберите категорию для удаления");

        HBox hBox = new HBox();
        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(event -> {
            CategoryDTO selectedCategory = comboBoxCategories.getValue();
            if (selectedCategory != null) {
                stage.setUserData(selectedCategory);
                stage.close();
            } else {
                // Если категория не выбрана, показываем ошибку
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, выберите категорию для удаления!");
                alert.showAndWait();
            }
        });

        Button cancelButton = new Button("Отмена");
        cancelButton.setOnAction(event -> {
            stage.setUserData(null); // Устанавливаем null, если пользователь отменил
            stage.close();
        });

        hBox.getChildren().addAll(deleteButton, cancelButton);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(label, comboBoxCategories, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();

        return (CategoryDTO) stage.getUserData(); // Возвращаем выбранную категорию или null
    }
}
