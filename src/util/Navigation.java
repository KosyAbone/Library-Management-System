package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class Navigation {

    private static Stage stage;

    // ✅ New version with controller injection support
    public static <T> void switchNavigation(String fxmlpath, ActionEvent event, Consumer<T> controllerHandler) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navigation.class.getResource("/view/" + fxmlpath));
        Parent root = loader.load();

        T controller = loader.getController();
        controllerHandler.accept(controller);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    // ✅ Original fallback version (no controller injection)
     public static void switchNavigation(String path, ActionEvent event) throws IOException {
        switchNavigation(path, event, controller -> {}); // empty lambda
    }

    // ✅ New version with controller injection for paging
    public static <T> void switchPaging(Pane pagingPane, String path, Consumer<T> controllerHandler) throws IOException {
    pagingPane.getChildren().clear();
    FXMLLoader loader = new FXMLLoader(Navigation.class.getResource("/view/" + path));
    Parent root = loader.load();

    // Anchor the child to all edges
    if (pagingPane instanceof AnchorPane) {
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
    }

    pagingPane.getChildren().add(root);

    T controller = loader.getController();
    controllerHandler.accept(controller);
}


    // ✅ Original fallback version (no controller injection)
    public static void switchPaging(Pane pane, String path) throws IOException {
        switchPaging(pane, path, controller -> {}); // empty lambda
    }

    // ✅ Popup already supports injection — unchanged
    public static <T> void openPopup(String fxmlPath, Consumer<T> controllerHandler) throws IOException {
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
