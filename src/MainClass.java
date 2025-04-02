import util.SceneManager;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize SceneManager with global dimensions
        SceneManager.init(primaryStage);
        
        SceneManager.loadAndSwitch("/view/Login.fxml");
        
        // Window configuration
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}