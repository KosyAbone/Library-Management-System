package controller;

import DAO.UserDAO;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.Navigation;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblUsernameAlert;
    @FXML private Label lblPasswordAlert;

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void btnSignInOnAction(ActionEvent event) {
        clearAlerts();

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty()) {
            lblUsernameAlert.setText("Username is required.");
            return;
        }

        if (password.isEmpty()) {
            lblPasswordAlert.setText("Password is required.");
            return;
        }

        User user = userDAO.login(username, password);
        
        System.out.println("user " + user );
        

        if (user != null) {
            navigateToDashboard(user, event);
        } else {
            lblPasswordAlert.setText("Invalid username or password.");
        }
    }

    private void navigateToDashboard(User user, ActionEvent event) {
        try {
            if (user.isAdmin()) {
                Navigation.switchNavigation("AdminDashboard.fxml", event); {
                
                System.out.println(user);
            }
            
            } else if (user.isMember()) {
              //  Navigation.switchNavigation("MemberDashboard.fxml", event); {
           // }
           Navigation.openPopup("MemberDashboard.fxml", (MemberDashboardController controller) -> {
            controller.setUser(user);
            
            System.out.println(user);
        });
                
            } else if (user.isLibrarian()) {
                Navigation.switchNavigation("LibrarianDashboard.fxml", event); {
            }
                
            } else {
                showAlert("Unknown user role: " + user.getUserType());
            }
        } catch (IOException e) {
            showAlert("Failed to load dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void btnSignUpOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Register.fxml", event);
        } catch (IOException e) {
            showAlert("Failed to open sign up: " + e.getMessage());
        }
    }

    @FXML
    private void hyperForgotPasswordOnAction(ActionEvent event) {
        showAlert("Forgot password flow not implemented yet.");
    }

    @FXML
    private void btnPowerOffOnAction(ActionEvent event) {
        Navigation.exit();
    }

    @FXML
    private void txtUsernameOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            txtPassword.requestFocus();
        }
    }

    @FXML
    private void txtPasswordOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnSignInOnAction(new ActionEvent());
        }
    }

    @FXML
    private void txtUsernameOnAction() {
        txtPassword.requestFocus();
    }

    @FXML
    private void txtPasswordOnAction() {
        btnSignInOnAction(new ActionEvent());
    }

    private void clearAlerts() {
        lblUsernameAlert.setText("");
        lblPasswordAlert.setText("");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
