package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmWindow {

    private static boolean answer;

    public static boolean PrintOut(String message)
    {
        Stage stage = new Stage();
        stage.setTitle("Подтверждение");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(windowEvent ->{
            answer = false;
        });
        
        VBox vBox = new VBox();
    
        HBox hBox = new HBox();
        Button yesButton = new Button("Да");
        yesButton.setOnAction(eventAction ->{
            answer = true;
            stage.close();
        });
        Button noButton = new Button("Нет");
        noButton.setOnAction(eventAction -> {
            answer = false;
            stage.close();
        });
        hBox.getChildren().addAll(yesButton, noButton);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().add(new Label(message));
        vBox.getChildren().add(hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        BorderPane stackPane = new BorderPane();
        stackPane.setCenter(vBox);

        Scene scene = new Scene(stackPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();

        return answer;
    }
}
