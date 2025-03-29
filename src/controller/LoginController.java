package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import Model.User;
import DAO.UserDAO;
import util.SceneManager;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    private final UserDAO userDAO = new UserDAO();
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        try {
            User user = userDAO.login(username, password);
            
            if (user == null) {
                showError("Invalid username or password");
                return;
            }
            
            switch (user.getUserType()) {
                case "ADMIN":
                    AdminDashboardController adminController = 
                        SceneManager.loadAndSwitch("/view/AdminDashboard.fxml");
                    adminController.setUser(user);
                    break;
                    
                case "LIBRARIAN":
                    LibrarianDashboardController librarianController = 
                        SceneManager.loadAndSwitch("/view/LibrarianDashboard.fxml");
                    librarianController.setUser(user);
                    break;
                    
                case "MEMBER":
                    MemberDashboardController memberController = 
                        SceneManager.loadAndSwitch("/view/MemberDashboard.fxml");
                    memberController.setUser(user);
                    break;
                    
                default:
                    showError("Unknown user type");
            }
        } catch (IOException e) {
            showError("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("An unexpected error occurred");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            RegisterController controller = SceneManager.loadAndSwitch("/view/Register.fxml");
            controller.setCurrentUser(null);
        } catch (IOException e) {
            showError("Error loading registration form: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}