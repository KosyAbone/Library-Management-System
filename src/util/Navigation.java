package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import controller.AdminMainController;
import controller.MemberMainController;
import java.util.function.Consumer;


import java.io.IOException;

public class Navigation {

    private static Stage stage;

    public static void switchNavigation(String path, ActionEvent event) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/" + path)));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void switchPaging(Pane pane, String path) throws IOException {
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(Navigation.class.getResource("/view/" + path));
        Parent root = loader.load();
        pane.getChildren().add(root);
    }
    
    public static <T> void openPopup(String fxmlPath, java.util.function.Consumer<T> controllerHandler) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navigation.class.getResource("/view/" + fxmlPath));
        Parent root = loader.load();

        T controller = loader.getController();
        controllerHandler.accept(controller);

        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root));
        popupStage.setTitle("Popup");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        popupStage.showAndWait();
}


    public static void close(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public static void exit() {
        System.exit(0);
    }
}