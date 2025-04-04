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
    
    public static void init(Stage primaryStage) {
        if (stage == null) {
            stage = primaryStage;
        }
    }
    
    public static void switchToScene(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
}
    
    public static <T> T loadAndSwitch(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        switchToScene(root);
        return loader.getController();
    }
    
    public static <T> T loadModal(String fxmlPath, String title, Modality modality, Consumer<T> configureController) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        T controller = loader.getController();
        
        if (configureController != null) {
            configureController.accept(controller);
        }

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