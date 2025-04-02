package util;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class SceneManager {
    private static Stage stage;
    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;

    // Initialize the stage once
    public static void init(Stage primaryStage) {
        if (stage == null) {
            stage = primaryStage;
            stage.setMinWidth(WIDTH);
            stage.setMinHeight(HEIGHT);
        }
    }

    // Core scene switching method
    public static void switchToScene(Parent root) {
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.centerOnScreen();
    }

    // Load FXML + switch scene + return controller
    public static <T> T loadAndSwitch(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        switchToScene(root);
        return loader.getController();
    }
    
    public static <T> T loadModal(String fxmlPath, String title, Modality modality, Consumer<T> configureController) throws IOException {
        
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        // Get controller before showing the stage
        T controller = loader.getController();
        
        // Configure the controller before showing the stage
        if (configureController != null) {
            configureController.accept(controller);
        }

        // Set up the scene
        // Create new stage for the modal
        Stage modalStage = new Stage();
        modalStage.initModality(modality);
        modalStage.setTitle(title);
        modalStage.setScene(new Scene(root));
        modalStage.showAndWait();

        return controller;
    }

    public static void showModal(Parent root, String title, Modality modality) {
        Stage modalStage = new Stage();
        modalStage.initModality(modality);
        modalStage.setTitle(title);
        modalStage.setScene(new Scene(root));
        modalStage.showAndWait();
    }
}