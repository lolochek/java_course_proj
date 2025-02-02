package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class GUI extends Application {

    private static Scene scene;
    private static final int width = 960;
    private static final int height = 540;
    private static FXMLLoader currentLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("authorization"), width, height);
        stage.setResizable(false);
        stage.setTitle("electronics store");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        currentLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));
        return currentLoader.load();
    }

    public static <T> T getController() {
        if (currentLoader != null) {
            return currentLoader.getController();
        }
        throw new IllegalStateException("FXMLLoader is not initialized. Call setRoot() or loadFXML() first.");
    }

    public static void main(String[] args) {
        launch();
    }
}
